package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Optional<TransactionEntity> findByTransactionUUID(UUID transactionUUID);

    Optional<TransactionEntity> findByRequestIdentifier(String requestIdentifier);
}
