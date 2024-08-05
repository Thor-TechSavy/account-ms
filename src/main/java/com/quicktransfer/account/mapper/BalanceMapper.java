package com.quicktransfer.account.mapper;

import com.quicktransfer.account.dto.BalanceDto;
import com.quicktransfer.account.entity.BalanceEntity;

public class BalanceMapper {

    private BalanceMapper() {
    }

    public static BalanceDto mapToDto(BalanceEntity balanceEntity) {
        var balanceDto = new BalanceDto();
        balanceDto.setBalance(balanceEntity.getAmount());
        balanceDto.setOwnerId(balanceEntity.getAccount().getOwnerId());
        balanceDto.setCurrency(balanceEntity.getAccount().getCurrency());
        balanceDto.setLastUpdate(balanceEntity.getLastUpdate());
        return balanceDto;
    }
}
