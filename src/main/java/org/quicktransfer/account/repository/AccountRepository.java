package org.quicktransfer.account.repository;

import jakarta.persistence.LockModeType;
import org.quicktransfer.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    AccountEntity findByOwnerId(UUID ownerId);
}
