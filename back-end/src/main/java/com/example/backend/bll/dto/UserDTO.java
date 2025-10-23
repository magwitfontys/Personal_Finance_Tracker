package com.example.backend.bll.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Integer id;
    private String username;
    private LocalDateTime createdAt;

    public UserDTO() {}
    public UserDTO(Integer id, String username, LocalDateTime createdAt) {
        this.id = id; this.username = username; this.createdAt = createdAt;
    }

    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Integer id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
