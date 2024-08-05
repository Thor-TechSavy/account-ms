package com.quicktransfer.account.service;

import com.quicktransfer.account.client.ExchangeRateClient;
import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import com.quicktransfer.account.exceptions.ExchangeRateException;
import com.quicktransfer.account.exceptions.InsufficientAccountBalanceException;
import com.quicktransfer.account.exceptions.TransactionException;
import com.quicktransfer.account.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ExchangeRateClient exchangeRateClient;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService, ExchangeRateClient exchangeRateClient) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.exchangeRateClient = exchangeRateClient;
    }

    /**
     * Creates and saves a new transaction entity in the repository.
     * <p>
     * This method takes a {@link TransactionEntity} object, which should be fully populated with
     * necessary details, and persists it to the database. The method returns the saved transaction
     * entity, which includes any generated identifiers or timestamps.
     *
     * @param transactionEntity The {@link TransactionEntity} object that represents the transaction
     *                          to be created. This entity should not be null and should be fully
     *                          initialized with all required fields.
     * @return The {@link TransactionEntity} that was saved to the repository, including any changes
     * made by the database (e.g., generated identifiers or timestamps).
     */
    public TransactionEntity createTransaction(TransactionEntity transactionEntity) {
        return transactionRepository.save(transactionEntity);
    }

    /**
     * Retrieves a transaction entity based on the given request identifier.
     * <p>
     * This method attempts to find a {@link TransactionEntity} in the repository that matches
     * the provided request identifier. It returns an {@link Optional} containing the transaction
     * entity if found, or an empty {@link Optional} if no matching entity is found.
     *
     * @param requestIdentifier The unique identifier associated with the transaction request.
     *                          This identifier is used to locate the corresponding transaction entity
     *                          in the repository. It should not be null or empty.
     * @return An {@link Optional} containing the {@link TransactionEntity} if a transaction with
     * the given request identifier exists; otherwise, an empty {@link Optional}.
     */
    public Optional<TransactionEntity> getTransaction(String requestIdentifier) {
        return transactionRepository.findByRequestIdentifier(requestIdentifier);
    }

    /**
     * Retrieves a {@link TransactionEntity} based on the given transaction UUID.
     * <p>
     * This method attempts to find a {@link TransactionEntity} in the repository that matches
     * the provided UUID. If a matching transaction is found, it is returned. If no matching
     * transaction is found, a {@link TransactionException} is thrown.
     *
     * @param transactionUUID The unique identifier for the transaction. This UUID is used to locate
     *                        the corresponding transaction entity in the repository.
     * @return The {@link TransactionEntity} associated with the provided UUID.
     * @throws TransactionException If no transaction with the specified UUID is found in the repository.
     */
    public TransactionEntity getTransaction(UUID transactionUUID) {

        return transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new TransactionException("not found for uuid: " + transactionUUID));
    }

    /**
     * Processes a financial transaction by performing debit and credit operations on the respective accounts.
     * <p>
     * This method handles the complete lifecycle of a transaction including:
     * <ul>
     *     <li>Retrieving the source and destination accounts.</li>
     *     <li>Validating the debit operation against the source account's balance.</li>
     *     <li>Applying the debit and credit operations with the appropriate exchange rate.</li>
     *     <li>Finalizing the transaction by setting its status and updating the transaction record.</li>
     * </ul>
     *
     * <p>The method is annotated with {@link Transactional}, ensuring that all operations are executed within
     * a single transaction context. If any exception occurs during the processing, the transaction is rolled back
     * and changes are reverted.</p>
     *
     * @param transaction The {@link TransactionEntity} representing the transaction details including
     *                    source and destination account identifiers, transaction amount, and currency information.
     * @return The updated {@link TransactionEntity} reflecting the final state of the transaction after processing.
     * @throws TransactionException If an error occurs during the transaction processing, such as insufficient funds,
     *                              or an error in retrieving exchange rates.
     */
    @Transactional
    public TransactionEntity processTransaction(TransactionEntity transaction) throws TransactionException {

        var fromAccount = accountService.getAccount(transaction.getFromOwnerId());
        var toAccount = accountService.getAccount(transaction.getToOwnerId());

        validateDebitAccountBalance(transaction.getTxnAmt(), fromAccount.getBalance().getAmount(),
                transaction.getFromOwnerId());

        var exchangeRate = getExchangeRate(fromAccount.getCurrency(), toAccount.getCurrency());

        performDebit(transaction.getTxnAmt(), fromAccount);
        performCredit(transaction.getTxnAmt(), exchangeRate, toAccount);

        finalizeTransaction(transaction, exchangeRate);

        return update(transaction);
    }

    private void performCredit(BigDecimal amount, double exchangeRate, AccountEntity toAccount) {
        var adjustedAmountToCredit = amount.multiply(BigDecimal.valueOf(exchangeRate));
        var amountAfterCredit = toAccount.getBalance().getAmount().add(adjustedAmountToCredit);
        toAccount.getBalance().setAmount(amountAfterCredit);
        accountService.updateAccount(toAccount);
    }

    private void performDebit(BigDecimal amount, AccountEntity fromAccount) {
        var amountAfterDebit = fromAccount.getBalance().getAmount().subtract(amount);
        fromAccount.getBalance().setAmount(amountAfterDebit);
        accountService.updateAccount(fromAccount);
    }

    private static void finalizeTransaction(TransactionEntity transaction, double exchangeRate) {
        transaction.setDebitOperation(transaction.getTxnAmt());
        transaction.setCreditOperation(transaction.getTxnAmt().multiply(BigDecimal.valueOf(exchangeRate)));
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
    }

    private double getExchangeRate(String sourceCurrency, String targetCurrency) {
        var exchangeRate = 1.0;
        if (!sourceCurrency.equals(targetCurrency)) {
            exchangeRate = exchangeRateClient
                    .getExchangeRate(sourceCurrency, targetCurrency)
                    .orElseThrow(() -> new ExchangeRateException("Exchange rate cannot be retrieved"));
        }

        return exchangeRate;
    }

    private static void validateDebitAccountBalance(BigDecimal debitAmount, BigDecimal availableBalance, UUID ownerId) {
        if (availableBalance.compareTo(debitAmount) < 0) {
            throw new InsufficientAccountBalanceException("Account doesn't have enough balance, ownerId: " + ownerId);
        }
    }

    public boolean checkIfTransactionAlreadyProcessedOrFailed(TransactionEntity transactionEntity) {
        return transactionEntity.getStatus() == TransactionStatus.SUCCESSFUL || transactionEntity.getStatus() == TransactionStatus.FAILED;
    }

    public TransactionEntity update(TransactionEntity transactionEntity) {

        transactionEntity.setLastUpdateTime(Instant.now());
        return transactionRepository.save(transactionEntity);
    }

}
