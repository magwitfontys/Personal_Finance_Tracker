package com.example.backend.bll.dto;

public class LoginResult {
    private boolean success;
    private String message;
    private UserDTO user;

    public LoginResult() {}
    public LoginResult(boolean success, String message, UserDTO user) {
        this.success = success; this.message = message; this.user = user;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
}
