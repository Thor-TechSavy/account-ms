package org.quicktransfer.account.repository;

import org.quicktransfer.account.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

    Optional<BalanceEntity> findByAccount_OwnerId(UUID ownerId);
}
