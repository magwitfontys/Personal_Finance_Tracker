package com.example.backend.pl.api;

import com.example.backend.bll.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionController_deleteAllTransactions_WithValidUserIdReturnsNoContent {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void deleteAllTransactions_WithValidUserId_ShouldReturn204() throws Exception {
        // Arrange
        Integer userId = 1;
        doNothing().when(transactionService).deleteAllByUserId(userId);

        // Act & Assert
        mockMvc.perform(
                delete("/api/transactions/delete-all")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNoContent());
    }

    
}
