package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.TransactionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TransactionEntity> findByTransactionUUID(UUID transactionUUID);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TransactionEntity> findByRequestIdentifier(String requestIdentifier);
}
