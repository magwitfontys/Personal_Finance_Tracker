package com.example.backend.pl.api;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthController_login_ValidCredentialsReturnsSuccess {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_WithValidCredentials_ShouldReturn200AndUserData() throws Exception {

        //arrange

        UserDTO mockUser = new UserDTO();
        mockUser.setId(123);
        mockUser.setUsername("testuser");
        when(authService.authenticate(anyString(), anyString()))
            .thenReturn(mockUser);
        String requestBody = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;

        //act & assert

        mockMvc.perform(
                post("/api/auth/login")                  
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)                    
            )
            .andExpect(status().isOk()) // if 200
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.userId").value(123));
    }
}
