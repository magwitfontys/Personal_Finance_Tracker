package com.example.backend.dal.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions", schema = "dbo", indexes = {
        @Index(name = "ix_transactions_id", columnList = "transaction_id", unique = true),
        @Index(name = "ix_transactions_user_id", columnList = "user_id"),
        @Index(name = "ix_transactions_category_id", columnList = "category_id"),
        @Index(name = "ix_transactions_txn_type", columnList = "txn_type")
})
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private Integer transactionId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "txn_type", nullable = false, length = 10)
    private String txnType;

    @Column(name = "txn_date", nullable = false)
    private LocalDate txnDate;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity() {}

    public TransactionEntity(Integer transactionId, Integer categoryId, Integer userId, BigDecimal amount, String description, String txnType, LocalDate txnDate, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.txnType = txnType;
        this.txnDate = txnDate;
        this.createdAt = createdAt;
    }
    public Integer getTransactionId() {
        return transactionId;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public Integer getUserId() {
        return userId;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public String getTxnType() {
        return txnType;
    }
    public LocalDate getTxnDate() {
        return txnDate;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }
    public void setTxnDate(LocalDate txnDate) {
        this.txnDate = txnDate;
    }
    public boolean isIncome() { 
        return "INCOME".equalsIgnoreCase(txnType); 
    }
    public boolean isExpense() { 
        return "EXPENSE".equalsIgnoreCase(txnType); 
    }

}
