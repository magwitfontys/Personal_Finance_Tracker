package com.example.backend.bll.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Integer transactionId;
    private Integer userId;
    private String description;
    private Integer categoryId;
    private String txnType; // "INCOME" or "EXPENSE"
    private BigDecimal amount;
    private LocalDate txnDate; 
    private LocalDateTime createdAt; 

    public TransactionDTO() {
    }
    public TransactionDTO(Integer transactionId, Integer userId, String description, Integer categoryId, String txnType, BigDecimal amount, LocalDate txnDate, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.description = description;
        this.categoryId = categoryId;
        this.txnType = txnType;
        this.amount = amount;
        this.txnDate = txnDate;
        this.createdAt = createdAt;
    }

    // getters
    public Integer getTransactionId() {
        return transactionId;
    }
    public Integer getUserId() {
        return userId;
    }
    public String getDescription() {
        return description;
    }
    public Integer getCategoryId() {
        return categoryId;
    }
    public String getTxnType() {
        return txnType;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public LocalDate getTxnDate() {
        return txnDate;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // setters
    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public void setTxnDate(LocalDate txnDate) {
        this.txnDate = txnDate;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
