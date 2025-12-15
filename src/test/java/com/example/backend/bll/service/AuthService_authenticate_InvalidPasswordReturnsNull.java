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
class AuthService_authenticate_InvalidPasswordReturnsNull {

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
    void authenticate_WithInvalidPassword_ShouldReturnNull() {
        // Arrange
        String username = "testuser";
        String rawPassword = "wrongpassword";
        String encodedPassword = "encodedPassword123";

        UserDTO user = new UserDTO();
        user.setId(1);
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);

        when(users.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        UserDTO result = authService.authenticate(username, rawPassword);

        // Assert
        assertNull(result);
        verify(users, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
    }
}
