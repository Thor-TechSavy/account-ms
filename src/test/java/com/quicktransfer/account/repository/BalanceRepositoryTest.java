package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.BalanceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BalanceRepositoryTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountRepository accountRepository;

    private AccountEntity accountEntity;
    private BalanceEntity balanceEntity;

    @BeforeEach
    void setUp() {
        // Create and save AccountEntity
        accountEntity = new AccountEntity();
        accountEntity.setFirstName("John");
        accountEntity.setLastName("Doe");
        accountEntity.setCurrency("USD");
        accountEntity.setDob("1990-01-01");
        accountRepository.save(accountEntity);

        // Create and save BalanceEntity
        balanceEntity = new BalanceEntity();
        balanceEntity.setAmount(BigDecimal.valueOf(1000.00));
        balanceEntity.setAccount(accountEntity);
        balanceEntity.setLastUpdate(Instant.now());
        balanceRepository.save(balanceEntity);
    }

    @Test
    void testFindByAccount_OwnerId() {
        Optional<BalanceEntity> foundEntity = balanceRepository.findByAccount_OwnerId(accountEntity.getOwnerId());

        assertTrue(foundEntity.isPresent());
        assertEquals(balanceEntity.getAmount(), foundEntity.get().getAmount());
        assertEquals(accountEntity.getOwnerId(), foundEntity.get().getAccount().getOwnerId());
        assertEquals(accountEntity.getFirstName(), foundEntity.get().getAccount().getFirstName());
        assertEquals(accountEntity.getLastName(), foundEntity.get().getAccount().getLastName());
        assertEquals(accountEntity.getCurrency(), foundEntity.get().getAccount().getCurrency());
        assertEquals(accountEntity.getDob(), foundEntity.get().getAccount().getDob());
    }
}
