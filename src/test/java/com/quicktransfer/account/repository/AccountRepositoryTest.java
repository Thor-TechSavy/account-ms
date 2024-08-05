package com.quicktransfer.account.repository;

import com.quicktransfer.account.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private AccountEntity accountEntity;

    @BeforeEach
    void setUp() {
        accountEntity = new AccountEntity();
        accountEntity.setFirstName("John");
        accountEntity.setLastName("Doe");
        accountEntity.setCurrency("USD");
        accountEntity.setDob("1990-01-01");
        accountRepository.save(accountEntity);
    }

    @Test
    void testFindByOwnerId() {
        Optional<AccountEntity> foundEntity = accountRepository.findByOwnerId(accountEntity.getOwnerId());

        assertTrue(foundEntity.isPresent());
        assertEquals(accountEntity.getOwnerId(), foundEntity.get().getOwnerId());
        assertEquals(accountEntity.getFirstName(), foundEntity.get().getFirstName());
        assertEquals(accountEntity.getLastName(), foundEntity.get().getLastName());
        assertEquals(accountEntity.getCurrency(), foundEntity.get().getCurrency());
        assertEquals(accountEntity.getDob(), foundEntity.get().getDob());
    }
}
