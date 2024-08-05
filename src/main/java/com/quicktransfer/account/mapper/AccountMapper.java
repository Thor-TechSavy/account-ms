package com.quicktransfer.account.mapper;

import com.quicktransfer.account.dto.AccountDetailsDto;
import com.quicktransfer.account.dto.CreateAccountDto;
import com.quicktransfer.account.entity.AccountEntity;

public class AccountMapper {

    private AccountMapper() {

    }

    public static AccountDetailsDto mapToDto(AccountEntity accountEntity) {
        var accountDetailsDto = new AccountDetailsDto();
        accountDetailsDto.setOwnerId(accountEntity.getOwnerId());
        accountDetailsDto.setCurrency(accountEntity.getCurrency());
        accountDetailsDto.setFirstName(accountEntity.getFirstName());
        accountDetailsDto.setLastName(accountEntity.getLastName());
        accountDetailsDto.setDob(accountEntity.getDob());
        return accountDetailsDto;
    }

    public static AccountEntity mapToEntity(CreateAccountDto accountDto) {
        var accountEntity = new AccountEntity();

        accountEntity.setCurrency(accountDto.getCurrency());
        accountEntity.setFirstName(accountDto.getFirstName());
        accountEntity.setLastName(accountDto.getLastName());
        accountEntity.setDob(accountDto.getDob());

        return accountEntity;
    }
}
