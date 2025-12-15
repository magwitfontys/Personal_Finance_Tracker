package com.example.backend.bll.service;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.port.out.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthService_register_SuccessfullyCreatesUser {

    @Mock
    private UserInterface users;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(users, passwordEncoder);
    }

    @Test
    void register_WithValidCredentials_ShouldCreateUser() {
        // Arrange
        String username = "newuser";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        when(users.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        UserDTO savedUser = new UserDTO();
        savedUser.setId(1);
        savedUser.setUsername(username);
        savedUser.setPasswordHash(encodedPassword);

        when(users.save(any(UserDTO.class))).thenReturn(savedUser);

        // Act
        UserDTO result = authService.register(username, rawPassword);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertNull(result.getPasswordHash()); // Password hash should be cleared
        verify(users, times(1)).existsByUsername(username);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(users, times(1)).save(any(UserDTO.class));
    }
}
