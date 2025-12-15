package com.example.backend.bll.service;

import com.example.backend.bll.dto.UserDTO;
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
class AuthService_deleteAccount_InvalidPasswordReturnsFalse {

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
    void deleteAccount_WithInvalidPassword_ShouldReturnFalse() {
        // Arrange
        Integer userId = 1;
        String rawPassword = "wrongpassword";
        String encodedPassword = "encodedPassword123";

        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setUsername("testuser");
        user.setPasswordHash(encodedPassword);

        when(users.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        boolean result = authService.deleteAccount(userId, rawPassword);

        // Assert
        assertFalse(result);
        verify(users, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(users, never()).deleteById(anyInt());
    }
}
