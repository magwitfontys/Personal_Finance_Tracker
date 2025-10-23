package com.example.backend.bll.service.impl;

import com.example.backend.bll.dto.*;
import com.example.backend.bll.service.AuthService;
import com.example.backend.dal.entity.UserEntity;
import com.example.backend.dal.repo.UserJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserJpaRepository users;

    public AuthServiceImpl(UserJpaRepository users) {
        this.users = users;
    }

    @Override
    public UserDTO register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String raw = request.getPassword();

        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

        // For this smoke test we store raw password in PasswordHash column.
        var entity = new UserEntity();
        entity.setUsername(username);
        entity.setPasswordHash(raw);
        entity.setCreatedAt(LocalDateTime.now());

        var saved = users.save(entity);
        return new UserDTO(saved.getId(), saved.getUsername(), saved.getCreatedAt());
    }

    @Override
    public LoginResult login(LoginRequest request) {
        var user = users.findByUsername(request.getUsername().trim())
                .orElse(null);
        if (user == null) {
            return new LoginResult(false, "Invalid username or password", null);
        }

        // Plain comparison for the smoke test.
        boolean ok = request.getPassword().equals(user.getPasswordHash());
        if (!ok) {
            return new LoginResult(false, "Invalid username or password", null);
        }

        var dto = new UserDTO(user.getId(), user.getUsername(), user.getCreatedAt());
        return new LoginResult(true, "OK", dto);
    }
}
