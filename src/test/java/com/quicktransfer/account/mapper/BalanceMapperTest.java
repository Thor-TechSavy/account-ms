package com.quicktransfer.account.mapper;

import com.quicktransfer.account.dto.BalanceDto;
import com.quicktransfer.account.entity.AccountEntity;
import com.quicktransfer.account.entity.BalanceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceMapperTest {

    @Test
    void testMapToDto() {
        // Set up the AccountEntity
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency("USD");

        // Set up the BalanceEntity
        BalanceEntity balanceEntity = new BalanceEntity();
        balanceEntity.setAmount(new BigDecimal("100.00"));
        balanceEntity.setAccount(accountEntity);
        Instant lastUpdate = Instant.now();
        balanceEntity.setLastUpdate(lastUpdate);

        // Map BalanceEntity to BalanceDto
        BalanceDto balanceDto = BalanceMapper.mapToDto(balanceEntity);

        // Verify the mappings
        assertEquals(balanceEntity.getAmount(), balanceDto.getBalance());
        assertEquals(balanceEntity.getAccount().getOwnerId(), balanceDto.getOwnerId());
        assertEquals(balanceEntity.getAccount().getCurrency(), balanceDto.getCurrency());
        assertEquals(balanceEntity.getLastUpdate(), balanceDto.getLastUpdate());
    }
}
