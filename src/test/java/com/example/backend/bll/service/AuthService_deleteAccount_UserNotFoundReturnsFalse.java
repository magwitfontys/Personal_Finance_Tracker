package com.example.backend.bll.service;

import com.example.backend.bll.port.out.UserInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthService_deleteAccount_UserNotFoundReturnsFalse {

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
    void deleteAccount_WithNonExistentUser_ShouldReturnFalse() {
        // Arrange
        Integer userId = 999;
        String rawPassword = "password123";

        when(users.findById(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = authService.deleteAccount(userId, rawPassword);

        // Assert
        assertFalse(result);
        verify(users, times(1)).findById(userId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(users, never()).deleteById(anyInt());
    }
}
