package com.example.backend.bll.service;

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
class AuthService_register_UsernameAlreadyExistsThrowsException {

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
    void register_WithExistingUsername_ShouldThrowException() {
        // Arrange
        String username = "existinguser";
        String rawPassword = "password123";

        when(users.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authService.register(username, rawPassword)
        );

        assertEquals("Username already exists", exception.getMessage());
        verify(users, times(1)).existsByUsername(username);
        verify(passwordEncoder, never()).encode(anyString());
        verify(users, never()).save(any());
    }
}
