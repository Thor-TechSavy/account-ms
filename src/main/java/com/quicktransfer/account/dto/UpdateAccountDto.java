package com.quicktransfer.account.dto;

import java.math.BigDecimal;

public class UpdateAccountDto {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
