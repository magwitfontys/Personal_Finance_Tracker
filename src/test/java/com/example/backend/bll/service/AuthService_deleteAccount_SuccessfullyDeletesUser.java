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
class AuthService_deleteAccount_SuccessfullyDeletesUser {

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
    void deleteAccount_WithValidPassword_ShouldDeleteUser() {
        // Arrange
        Integer userId = 1;
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setUsername("testuser");
        user.setPasswordHash(encodedPassword);

        when(users.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(users.deleteById(userId)).thenReturn(true);

        // Act
        boolean result = authService.deleteAccount(userId, rawPassword);

        // Assert
        assertTrue(result);
        verify(users, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(users, times(1)).deleteById(userId);
    }
}
