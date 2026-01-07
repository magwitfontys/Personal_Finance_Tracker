package com.example.backend.pl.api;

import com.example.backend.bll.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthController_deleteAccount_ValidPasswordReturnsNoContent {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void deleteAccount_WithValidPassword_ShouldReturn204() throws Exception {
        // Arrange
        Integer userId = 1;
        String password = "correctPassword123";

        when(authService.deleteAccount(userId, password))
                .thenReturn(true);

        String requestBody = """
                {
                    "userId": %d,
                    "password": "%s"
                }
                """.formatted(userId, password);

        // Act & Assert
        mockMvc.perform(
                delete("/api/auth/delete-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isNoContent());
    }

    
}
