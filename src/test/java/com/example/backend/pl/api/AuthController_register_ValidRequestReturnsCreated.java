package com.example.backend.pl.api;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthController_register_ValidRequestReturnsCreated {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void register_WithValidCredentials_ShouldReturn201AndLocation() throws Exception {
        // Arrange
        String username = "newuser";
        String password = "securePassword123";
        
        UserDTO createdUser = new UserDTO();
        createdUser.setId(5);
        createdUser.setUsername(username);

        when(authService.register(username, password))
                .thenReturn(createdUser);

        String requestBody = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        // Act & Assert
        mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/api/users/5"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.username").value(username));
    }

    
}
