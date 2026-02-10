package com.jpmc.midascore.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserRecord sender;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserRecord recipient;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private float incentiveAmount;

    protected TransactionRecord() {
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentiveAmount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.createdAt = Instant.now();
        this.incentiveAmount = incentiveAmount;
    }

    public long getId() {
        return id;
    }

    public UserRecord getSender() {
        return sender;
    }

    public UserRecord getRecipient() {
        return recipient;
    }

    public float getAmount() {
        return amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public float getIncentiveAmount() {
        return incentiveAmount;
    }
}
