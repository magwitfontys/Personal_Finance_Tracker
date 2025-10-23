package com.example.backend.dal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users", schema = "dbo")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SQL Server IDENTITY
    @Column(name = "User_ID", nullable = false)
    private Integer id;

    @Column(name = "Username", nullable = false, length = 100)
    private String username;

    // No explicit length to match NVARCHAR(MAX) comfortably
    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(name = "CreatedAt", nullable = true)
    private LocalDateTime createdAt;

    // --- JPA requires a no-args constructor; make it PUBLIC so BLL can instantiate
    // it
    public UserEntity() {
    }

    // --- Convenience constructor (optional)
    public UserEntity(String username, String passwordHash, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    // --- Fallback to set CreatedAt if not provided (helps when column is NOT NULL)
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
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
