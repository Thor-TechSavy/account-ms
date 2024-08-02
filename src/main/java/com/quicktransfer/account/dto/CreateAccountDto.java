package com.quicktransfer.account.dto;

import java.math.BigDecimal;

public class CreateAccountDto {

    private String firstName;
    private String lastName;
    private String dob;
    private String currency;
    private BigDecimal balance;

    public CreateAccountDto() {}

    public CreateAccountDto(String firstName, String lastName, String currency, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.currency = currency;
        this.dob = dob;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
