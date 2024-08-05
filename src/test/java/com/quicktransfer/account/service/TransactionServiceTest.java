package com.quicktransfer.account.service;

import com.quicktransfer.account.client.ExchangeRateClient;
import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.BalanceEntity;
import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import com.quicktransfer.account.exceptions.ExchangeRateException;
import com.quicktransfer.account.exceptions.InsufficientAccountBalanceException;
import com.quicktransfer.account.repository.TransactionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionEntity transactionEntity;
    private AccountEntity fromAccount;
    private AccountEntity toAccount;
    private UUID fromOwnerId;
    private UUID toOwnerId;

    @BeforeEach
    void setUp() {
        fromOwnerId = UUID.randomUUID();
        toOwnerId = UUID.randomUUID();

        fromAccount = new AccountEntity();
        BalanceEntity fromBalance = new BalanceEntity();
        fromBalance.setAmount(new BigDecimal("1000.00"));
        fromAccount.setBalance(fromBalance);
        fromAccount.setCurrency("EUR");

        toAccount = new AccountEntity();
        BalanceEntity toBalance = new BalanceEntity();
        toBalance.setAmount(new BigDecimal("500.00"));
        toAccount.setBalance(toBalance);
        toAccount.setCurrency("USD");


        transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(fromOwnerId);
        transactionEntity.setToOwnerId(toOwnerId);
        transactionEntity.setTxnAmt(new BigDecimal("100.00"));
        transactionEntity.setStatus(TransactionStatus.PROCESSING);
    }

    @AfterEach
    void tearDown() {
        reset(
                transactionRepository,
                accountService,
                exchangeRateClient
        );
    }

    @Test
    void testCreateTransaction() {
        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);

        TransactionEntity savedTransaction = transactionService.createTransaction(transactionEntity);

        verify(transactionRepository, times(1)).save(transactionEntity);
        assertEquals(transactionEntity, savedTransaction);
    }

    @Test
    void testGetTransaction() {
        String requestIdentifier = "request-123";
        when(transactionRepository.findByRequestIdentifier(requestIdentifier)).thenReturn(Optional.of(transactionEntity));

        Optional<TransactionEntity> retrievedTransaction = transactionService.getTransaction(requestIdentifier);

        verify(transactionRepository, times(1)).findByRequestIdentifier(requestIdentifier);
        assertTrue(retrievedTransaction.isPresent());
        assertEquals(transactionEntity, retrievedTransaction.get());
    }

    @Test
    void testGetTransactionNotFound() {
        String requestIdentifier = "request-123";
        when(transactionRepository.findByRequestIdentifier(requestIdentifier)).thenReturn(Optional.empty());

        Optional<TransactionEntity> retrievedTransaction = transactionService.getTransaction(requestIdentifier);

        verify(transactionRepository, times(1)).findByRequestIdentifier(requestIdentifier);
        assertFalse(retrievedTransaction.isPresent());
    }

    @Test
    void testProcessTransaction() {
        when(accountService.getAccount(fromOwnerId)).thenReturn(fromAccount);
        when(accountService.getAccount(toOwnerId)).thenReturn(toAccount);
        when(exchangeRateClient.getExchangeRate("EUR", "USD")).thenReturn(Optional.of(1.0));
        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);

        TransactionEntity processedTransaction = transactionService.processTransaction(transactionEntity);

        verify(accountService, times(1)).getAccount(fromOwnerId);
        verify(accountService, times(1)).getAccount(toOwnerId);
        verify(accountService, times(1)).updateAccount(fromAccount);
        verify(accountService, times(1)).updateAccount(toAccount);
        verify(transactionRepository, times(1)).save(transactionEntity);

        assertEquals(TransactionStatus.SUCCESSFUL, processedTransaction.getStatus());
        assertEquals(transactionEntity.getTxnAmt(), processedTransaction.getDebitOperation());
    }

    @Test
    void testProcessTransactionInsufficientBalance() {
        transactionEntity.setTxnAmt(new BigDecimal("2000.00"));
        when(accountService.getAccount(fromOwnerId)).thenReturn(fromAccount);

        assertThrows(InsufficientAccountBalanceException.class, () -> transactionService.processTransaction(transactionEntity));
    }

    @Test
    void testProcessTransactionExchangeRateException() {
        when(accountService.getAccount(fromOwnerId)).thenReturn(fromAccount);
        when(accountService.getAccount(toOwnerId)).thenReturn(toAccount);
        when(exchangeRateClient.getExchangeRate("EUR", "USD")).thenReturn(Optional.empty());

        assertThrows(ExchangeRateException.class, () -> transactionService.processTransaction(transactionEntity));
    }
}
