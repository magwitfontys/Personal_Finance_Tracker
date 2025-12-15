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
class AuthService_getById_UserNotFoundReturnsEmpty {

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
    void getById_WithNonExistentId_ShouldReturnEmpty() {
        // Arrange
        Integer userId = 999;

        when(users.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<com.example.backend.bll.dto.UserDTO> result = authService.getById(userId);

        // Assert
        assertTrue(result.isEmpty());
        verify(users, times(1)).findById(userId);
    }
}
