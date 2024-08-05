package com.quicktransfer.account.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ownerId", nullable = false, unique = true)
    private UUID ownerId = UUID.randomUUID();

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "creationTime", nullable = false)
    private Instant creationTime = Instant.now();

    @Column(name = "lastUpdateTime")
    private Instant lastUpdateTime;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private BalanceEntity balance;

    public Long getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
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

    public Instant getCreationTime() {
        return creationTime;
    }

    public Instant getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Instant lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BalanceEntity getBalance() {
        return balance;
    }

    public void setBalance(BalanceEntity balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(ownerId, that.ownerId) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(dob, that.dob) && Objects.equals(currency, that.currency) && Objects.equals(creationTime, that.creationTime) && Objects.equals(lastUpdateTime, that.lastUpdateTime) && Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob='" + dob + '\'' +
                ", currency='" + currency + '\'' +
                ", creationTime=" + creationTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", balance=" + balance +
                '}';
    }
}
