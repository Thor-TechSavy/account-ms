package org.quicktransfer.account.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "Transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "fromOwnerId", nullable = false)
    private UUID fromOwnerId;

    @Column(name = "toOwnerId", nullable = false)
    private UUID toOwnerId;

    @Column(name = "debitOperation", nullable = false)
    private BigDecimal debitOperation;

    @Column(name = "creditOperation", nullable = false)
    private BigDecimal creditOperation;

    @Column(name = "requestIdentifier")
    private String requestIdentifier;

    @Column(name = "status", nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
