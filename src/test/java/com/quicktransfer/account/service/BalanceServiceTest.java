package com.quicktransfer.account.service;

import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.BalanceEntity;
import com.quicktransfer.account.repository.BalanceRepository;
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
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    private BalanceEntity balanceEntity;
    private UUID ownerId;

    @Captor
    private ArgumentCaptor<BalanceEntity> balanceEntityCaptor;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        balanceEntity = new BalanceEntity();
        balanceEntity.setAmount(new BigDecimal("100.00"));
        balanceEntity.setLastUpdate(Instant.now());
        balanceEntity.setAccount(new AccountEntity());
    }

    @AfterEach
    void tearDown() {
        reset(
                balanceRepository
        );
    }

    @Test
    void testGetBalance() {
        when(balanceRepository.findByAccount_OwnerId(any(UUID.class))).thenReturn(Optional.of(balanceEntity));

        BalanceEntity retrievedBalance = balanceService.getBalance(ownerId);

        verify(balanceRepository, times(1)).findByAccount_OwnerId(ownerId);
        assertEquals(balanceEntity, retrievedBalance);
    }

    @Test
    void testGetBalanceNotFound() {
        when(balanceRepository.findByAccount_OwnerId(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> balanceService.getBalance(ownerId));
    }

    @Test
    void testUpdateBalance() {
        when(balanceRepository.findByAccount_OwnerId(any(UUID.class))).thenReturn(Optional.of(balanceEntity));
        when(balanceRepository.save(any(BalanceEntity.class))).thenReturn(balanceEntity);

        BigDecimal amountToAdd = new BigDecimal("50.00");
        BalanceEntity updatedBalance = balanceService.updateBalance(ownerId, amountToAdd);

        verify(balanceRepository, times(1)).findByAccount_OwnerId(ownerId);
        verify(balanceRepository, times(1)).save(balanceEntityCaptor.capture());
        BalanceEntity capturedBalance = balanceEntityCaptor.getValue();

        assertEquals(balanceEntity.getAmount(), capturedBalance.getAmount());
        assertNotNull(capturedBalance.getLastUpdate());
        assertEquals(balanceEntity, updatedBalance);
    }

    @Test
    void testUpdateBalanceNotFound() {
        when(balanceRepository.findByAccount_OwnerId(any(UUID.class))).thenReturn(Optional.empty());

        BigDecimal amountToAdd = new BigDecimal("50.00");

        assertThrows(RuntimeException.class, () -> balanceService.updateBalance(ownerId, amountToAdd));
    }
}
