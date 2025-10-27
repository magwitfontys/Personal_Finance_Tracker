package com.example.backend.bll.service;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.port.out.UserInterface;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserInterface users;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserInterface users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    public UserDTO register(String username, String rawPassword) {
        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        String passwordHash = passwordEncoder.encode(rawPassword);

        var toCreate = new UserDTO();
        toCreate.setId(null);
        toCreate.setUsername(username);
        toCreate.setPasswordHash(passwordHash);
        toCreate.setCreatedAt(null); // DAL sets it

        var saved = users.save(toCreate);
        saved.setPasswordHash(null); // never return the hash
        return saved;
    }

    // LOGIN
    public boolean login(String username, String rawPassword) {
        Optional<UserDTO> found = users.findByUsername(username);
        if (found.isEmpty()) {
            return false;
        }
        var dto = found.get();
        return passwordEncoder.matches(rawPassword, dto.getPasswordHash());
    }

    public Optional<UserDTO> getById(Integer id) {
        return users.findById(id);
    }
}
