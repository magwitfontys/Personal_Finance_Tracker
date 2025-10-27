package com.example.backend.bll.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private Integer id;
    private String username;
    private LocalDateTime createdAt;
    @JsonIgnore
    private String passwordHash;

    public UserDTO() {

    }

    public UserDTO(Integer id, String username, LocalDateTime createdAt, String passwordHash) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.passwordHash = passwordHash;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
