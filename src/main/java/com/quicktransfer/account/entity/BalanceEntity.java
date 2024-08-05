package com.quicktransfer.account.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "BALANCE")
public class BalanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance")
    private BigDecimal amount;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "ownerId")
    private AccountEntity account;

    @Column(name = "lastUpdate")
    private Instant lastUpdate;

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceEntity that = (BalanceEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, account);
    }

    @Override
    public String toString() {
        return "BalanceEntity{" +
                "id=" + id +
                ", amount=" + amount +
                ", account=" + account +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
