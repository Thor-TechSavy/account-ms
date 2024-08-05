package com.quicktransfer.account.mapper;

import com.quicktransfer.account.dto.AccountDetailsDto;
import com.quicktransfer.account.dto.CreateAccountDto;
import com.quicktransfer.account.entity.AccountEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {

    @Test
    void testMapToDto() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency("USD");
        accountEntity.setFirstName("John");
        accountEntity.setLastName("Doe");
        accountEntity.setDob("17-10-2000");

        AccountDetailsDto accountDetailsDto = AccountMapper.mapToDto(accountEntity);

        assertEquals(accountEntity.getOwnerId(), accountDetailsDto.getOwnerId());
        assertEquals(accountEntity.getCurrency(), accountDetailsDto.getCurrency());
        assertEquals(accountEntity.getFirstName(), accountDetailsDto.getFirstName());
        assertEquals(accountEntity.getLastName(), accountDetailsDto.getLastName());
        assertEquals(accountEntity.getDob(), accountDetailsDto.getDob());
    }

    @Test
    void testMapToEntity() {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setCurrency("USD");
        createAccountDto.setFirstName("Jane");
        createAccountDto.setLastName("Doe");
        createAccountDto.setDob("17-10-2000");

        AccountEntity accountEntity = AccountMapper.mapToEntity(createAccountDto);

        assertEquals(createAccountDto.getCurrency(), accountEntity.getCurrency());
        assertEquals(createAccountDto.getFirstName(), accountEntity.getFirstName());
        assertEquals(createAccountDto.getLastName(), accountEntity.getLastName());
        assertEquals(createAccountDto.getDob(), accountEntity.getDob());
    }
}
