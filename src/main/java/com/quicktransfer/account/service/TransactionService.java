package com.quicktransfer.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quicktransfer.account.client.ExchangeRateClient;
import com.quicktransfer.account.dto.RequestIdentifier;
import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.dto.RequestIdentifierDto;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import com.quicktransfer.account.exceptions.AccountNotFoundException;
import com.quicktransfer.account.exceptions.ExchangeRateException;
import com.quicktransfer.account.exceptions.InsufficientAccountBalanceException;
import com.quicktransfer.account.exceptions.TransactionException;
import com.quicktransfer.account.repository.TransactionRepository;
import com.quicktransfer.account.util.JsonUtil;
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

    @Transactional
    public TransactionEntity creditAndDebitOperation(RequestIdentifier transactionDto) {
        String s = null;
        try {
            s = JsonUtil.getMapper().writeValueAsString(transactionDto.getRequestIdentifier());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional<TransactionEntity> existingTransaction = transactionRepository.findByRequestIdentifier(s);
        if (existingTransaction.isPresent() && existingTransaction.get().getStatus() == TransactionStatus.SUCCESSFUL) {
            return existingTransaction.get();
        }

        TransactionEntity transaction = createTransaction(transactionDto);

        try {
            AccountEntity fromAccount = accountService.findAccountByOwnerId(transactionDto.getFromOwnerId());
            AccountEntity toAccount = accountService.findAccountByOwnerId(transactionDto.getToOwnerId());

            validateDebitAccountBalance(
                    transactionDto.getAmount(),
                    fromAccount.getBalance().getAmount(),
                    transactionDto.getFromOwnerId());

            double exchangeRate = getExchangeRate(fromAccount.getCurrency(), toAccount.getCurrency());

            BigDecimal amountAfterDebit = fromAccount.getBalance().getAmount().subtract(transactionDto.getAmount());
            fromAccount.getBalance().setAmount(amountAfterDebit);
            accountService.updateAccount(fromAccount);

            BigDecimal adjustedCreditAmount = transactionDto
                    .getAmount()
                    .multiply(BigDecimal.valueOf(exchangeRate));

            BigDecimal amountAfterCredit = toAccount
                    .getBalance()
                    .getAmount()
                    .add(adjustedCreditAmount);

            toAccount.getBalance().setAmount(amountAfterCredit);
            accountService.updateAccount(toAccount);

            transaction.setDebitOperation(transactionDto.getAmount());
            transaction.setCreditOperation(adjustedCreditAmount);
            transaction.setStatus(TransactionStatus.SUCCESSFUL);

        } catch (InsufficientAccountBalanceException | AccountNotFoundException | ExchangeRateException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            update(transaction);

        } finally {
            update(transaction);
        }

        return transaction;
    }

    private double getExchangeRate(String sourceCurrency, String targetCurrency) {
        double exchangeRate = 0.0;
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


    public TransactionEntity createTransaction(RequestIdentifier transactionDto) {

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(transactionDto.getFromOwnerId());
        transactionEntity.setToOwnerId(transactionDto.getToOwnerId());

        try {
            String jsonIdentifier = JsonUtil.getMapper().writeValueAsString(transactionDto.getRequestIdentifier());
            transactionEntity.setRequestIdentifier(jsonIdentifier);
        } catch (JsonProcessingException e) {
            throw new TransactionException(e.getMessage());
        }
        transactionEntity.setStatus(TransactionStatus.IN_PROGRESS);
        return transactionRepository.save(transactionEntity);
    }

    public TransactionEntity findTransactionByTransactionUUID(UUID transactionUUID) {

        return transactionRepository.findByTransactionUUID(transactionUUID)
                .orElseThrow(() -> new TransactionException("not found for uuid: " + transactionUUID));


    }

    public void update(TransactionEntity transactionEntity) {

        transactionEntity.setLastUpdateTime(Instant.now());
        transactionRepository.save(transactionEntity);
    }


}
