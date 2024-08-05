package com.quicktransfer.account.entity;

import com.quicktransfer.account.enums.TransactionStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transactionId", nullable = false, unique = true)
    private UUID transactionUUID = UUID.randomUUID();

    @Column(name = "fromOwnerId", nullable = false)
    private UUID fromOwnerId;

    @Column(name = "toOwnerId", nullable = false)
    private UUID toOwnerId;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal txnAmt;

    @Column(name = "debitOperation")
    private BigDecimal debitOperation;

    @Column(name = "creditOperation")
    private BigDecimal creditOperation;

    @Column(name = "requestIdentifier", nullable = false, unique = true)
    private String requestIdentifier;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "creationTime", nullable = false)

    private Instant creationTime = Instant.now();

    @Column(name = "lastUpdateTime")

    private Instant lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getFromOwnerId() {
        return fromOwnerId;
    }

    public void setFromOwnerId(UUID fromOwnerId) {
        this.fromOwnerId = fromOwnerId;
    }

    public UUID getToOwnerId() {
        return toOwnerId;
    }

    public void setToOwnerId(UUID toOwnerId) {
        this.toOwnerId = toOwnerId;
    }

    public BigDecimal getDebitOperation() {
        return debitOperation;
    }

    public void setDebitOperation(BigDecimal debitOperation) {
        this.debitOperation = debitOperation;
    }

    public BigDecimal getCreditOperation() {
        return creditOperation;
    }

    public void setCreditOperation(BigDecimal creditOperation) {
        this.creditOperation = creditOperation;
    }

    public String getRequestIdentifier() {
        return requestIdentifier;
    }

    public void setRequestIdentifier(String requestIdentifier) {
        this.requestIdentifier = requestIdentifier;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public UUID getTransactionUUID() {
        return transactionUUID;
    }

    public BigDecimal getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(BigDecimal txnAmt) {
        this.txnAmt = txnAmt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(transactionUUID, that.transactionUUID) && Objects.equals(fromOwnerId, that.fromOwnerId) && Objects.equals(toOwnerId, that.toOwnerId) && Objects.equals(requestIdentifier, that.requestIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionUUID, fromOwnerId, toOwnerId, requestIdentifier);
    }

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "id=" + id +
                ", transactionUUID=" + transactionUUID +
                ", fromOwnerId=" + fromOwnerId +
                ", toOwnerId=" + toOwnerId +
                ", txnAmt=" + txnAmt +
                ", debitOperation=" + debitOperation +
                ", creditOperation=" + creditOperation +
                ", requestIdentifier='" + requestIdentifier + '\'' +
                ", status=" + status +
                ", creationTime=" + creationTime +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
