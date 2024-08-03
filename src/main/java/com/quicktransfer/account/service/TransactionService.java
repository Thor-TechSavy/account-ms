package com.quicktransfer.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public TransactionEntity createTransaction(TransactionEntity transactionEntity) {

        return transactionRepository.save(transactionEntity);
    }

    public Optional<TransactionEntity> getTransaction(String requestIdentifier) {
        return transactionRepository.findByRequestIdentifier(requestIdentifier);
    }

    public TransactionEntity getTransaction(UUID transactionUUID) {

        return transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new TransactionException("not found for uuid: " + transactionUUID));
    }

    @Transactional
    public TransactionEntity processTransaction(TransactionEntity transaction) throws TransactionException {

        AccountEntity fromAccount = accountService.getAccount(transaction.getFromOwnerId());
        AccountEntity toAccount = accountService.getAccount(transaction.getToOwnerId());

        validateDebitAccountBalance(transaction.getTxnAmt(), fromAccount.getBalance().getAmount(),
                transaction.getFromOwnerId());

        double exchangeRate = getExchangeRate(fromAccount.getCurrency(), toAccount.getCurrency());

        performDebit(transaction.getTxnAmt(), fromAccount);
        performCredit(transaction.getTxnAmt(), exchangeRate, toAccount);

        finalizeTransaction(transaction, exchangeRate);

        return update(transaction);
    }

    private void performCredit(BigDecimal amount, double exchangeRate, AccountEntity toAccount) {
        BigDecimal adjustedAmountToCredit = amount.multiply(BigDecimal.valueOf(exchangeRate));
        BigDecimal amountAfterCredit = toAccount.getBalance().getAmount().add(adjustedAmountToCredit);
        toAccount.getBalance().setAmount(amountAfterCredit);
        accountService.updateAccount(toAccount);
    }

    private void performDebit(BigDecimal amount, AccountEntity fromAccount) {
        BigDecimal amountAfterDebit = fromAccount.getBalance().getAmount().subtract(amount);
        fromAccount.getBalance().setAmount(amountAfterDebit);
        accountService.updateAccount(fromAccount);
    }

    private static void finalizeTransaction(TransactionEntity transaction, double exchangeRate) {
        transaction.setDebitOperation(transaction.getTxnAmt());
        transaction.setCreditOperation(transaction.getTxnAmt().multiply(BigDecimal.valueOf(exchangeRate)));
        transaction.setStatus(TransactionStatus.SUCCESSFUL);
    }

    private double getExchangeRate(String sourceCurrency, String targetCurrency) {
        double exchangeRate = 1.0;
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

    public TransactionEntity update(TransactionEntity transactionEntity) {

        transactionEntity.setLastUpdateTime(Instant.now());
        return transactionRepository.save(transactionEntity);
    }

}
