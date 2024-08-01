package org.quicktransfer.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountDetailsDto {

    private UUID ownerId;
    private String firstName;
    private String lastName;
    private String dob;
    private String currency;
    private BigDecimal balance;

    public AccountDetailsDto() {

    }

    public AccountDetailsDto(String firstName, String lastName, String dob, String currency, BigDecimal balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.currency = currency;
        this.balance = balance;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }
}
