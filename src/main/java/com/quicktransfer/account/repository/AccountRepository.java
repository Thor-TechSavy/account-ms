package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findByOwnerId(UUID ownerId);
}
