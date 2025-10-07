package com.example.backend.dal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users", schema = "dbo")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SQL Server IDENTITY
    @Column(name = "User_ID", nullable = false)
    private Integer id; // SQL int → Integer

    @Column(name = "Username", nullable = false, length = 100)
    private String username;

    @Column(name = "PasswordHash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "CreatedAt") // nullable in your schema
    private LocalDateTime createdAt;

    // --- JPA needs a no-args constructor
    protected UserEntity() {
    }

    // --- Convenience constructor (optional)
    public UserEntity(String username, String passwordHash, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    // --- Getters/Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
