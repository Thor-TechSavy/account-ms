package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.TransactionEntity;
import com.quicktransfer.account.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private TransactionEntity transactionEntity;

    @BeforeEach
    void setUp() {
        // Create and save TransactionEntity
        transactionEntity = new TransactionEntity();
        transactionEntity.setFromOwnerId(UUID.randomUUID());
        transactionEntity.setToOwnerId(UUID.randomUUID());
        transactionEntity.setTxnAmt(BigDecimal.valueOf(100.00));
        transactionEntity.setRequestIdentifier("request-12345");
        transactionEntity.setStatus(TransactionStatus.SUCCESSFUL);
        transactionRepository.save(transactionEntity);
    }

    @Test
    void testFindByTransactionUUID() {
        Optional<TransactionEntity> foundEntity = transactionRepository.findByTransactionUUID(transactionEntity.getTransactionUUID());

        assertTrue(foundEntity.isPresent());
        assertEquals(transactionEntity.getTransactionUUID(), foundEntity.get().getTransactionUUID());
        assertEquals(transactionEntity.getFromOwnerId(), foundEntity.get().getFromOwnerId());
        assertEquals(transactionEntity.getToOwnerId(), foundEntity.get().getToOwnerId());
        assertEquals(transactionEntity.getTxnAmt(), foundEntity.get().getTxnAmt());
        assertEquals(transactionEntity.getRequestIdentifier(), foundEntity.get().getRequestIdentifier());
        assertEquals(transactionEntity.getStatus(), foundEntity.get().getStatus());
    }

    @Test
    void testFindByRequestIdentifier() {
        Optional<TransactionEntity> foundEntity = transactionRepository.findByRequestIdentifier(transactionEntity.getRequestIdentifier());

        assertTrue(foundEntity.isPresent());
        assertEquals(transactionEntity.getTransactionUUID(), foundEntity.get().getTransactionUUID());
        assertEquals(transactionEntity.getFromOwnerId(), foundEntity.get().getFromOwnerId());
        assertEquals(transactionEntity.getToOwnerId(), foundEntity.get().getToOwnerId());
        assertEquals(transactionEntity.getTxnAmt(), foundEntity.get().getTxnAmt());
        assertEquals(transactionEntity.getRequestIdentifier(), foundEntity.get().getRequestIdentifier());
        assertEquals(transactionEntity.getStatus(), foundEntity.get().getStatus());
    }
}
