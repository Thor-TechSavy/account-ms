package com.quicktransfer.account.service;

import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.exceptions.AccountNotFoundException;
import com.quicktransfer.account.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountEntity accountEntity;

    @Captor
    private ArgumentCaptor<AccountEntity> accountEntityCaptor;

    @BeforeEach
    void setUp() {
        accountEntity = new AccountEntity();
        accountEntity.setCurrency("USD");
        accountEntity.setFirstName("John");
        accountEntity.setLastName("Doe");
        accountEntity.setDob("1980-01-01");
    }

    @AfterEach
    void tearDown() {
        reset(
                accountRepository
        );
    }

    @Test
    void testCreateAccount() {
        when(accountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        AccountEntity createdAccount = accountService.createAccount(accountEntity);

        verify(accountRepository, times(1)).save(accountEntityCaptor.capture());
        AccountEntity capturedAccount = accountEntityCaptor.getValue();

        assertNotNull(capturedAccount.getBalance());
        assertEquals(BigDecimal.ZERO, capturedAccount.getBalance().getAmount());
        assertEquals(accountEntity, capturedAccount.getBalance().getAccount());
        assertNotNull(capturedAccount.getBalance().getLastUpdate());
        assertEquals(accountEntity, createdAccount);
    }

    @Test
    void testGetAccount() {
        var uuid = UUID.randomUUID();
        when(accountRepository.findByOwnerId(any(UUID.class))).thenReturn(Optional.of(accountEntity));

        AccountEntity foundAccount = accountService.getAccount(uuid);

        verify(accountRepository, times(1)).findByOwnerId(uuid);
        assertEquals(accountEntity, foundAccount);
    }

    @Test
    void testGetAccountNotFound() {
        var uuid = UUID.randomUUID();

        when(accountRepository.findByOwnerId(any(UUID.class))).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccount(uuid);
        });

        assertEquals("Account doesn't exist for owner id: " + uuid, exception.getMessage());
    }

    @Test
    void testUpdateAccount() {
        accountService.updateAccount(accountEntity);

        verify(accountRepository, times(1)).save(accountEntityCaptor.capture());
        AccountEntity capturedAccount = accountEntityCaptor.getValue();

        assertNotNull(capturedAccount.getLastUpdateTime());
        assertEquals(accountEntity.getLastUpdateTime(), capturedAccount.getLastUpdateTime());
    }
}
