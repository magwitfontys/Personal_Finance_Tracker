package com.example.backend.pl.api;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionController_createTransaction_ValidRequestReturnsCreated {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void createTransaction_WithValidExpense_ShouldReturn201WithLocation() throws Exception {
        // Arrange
        Integer userId = 1;
        Integer categoryId = 2;
        BigDecimal amount = new BigDecimal("75.50");
        String txnType = "EXPENSE";
        LocalDate txnDate = LocalDate.now();
        String description = "Weekly groceries";

        TransactionDTO createdTx = new TransactionDTO(10, userId, description, categoryId, txnType, amount, txnDate, null);

        when(transactionService.save(any(TransactionDTO.class))).thenReturn(createdTx);

        String requestBody = """
                {
                    "userId": %d,
                    "categoryId": %d,
                    "amount": %.2f,
                    "txnType": "%s",
                    "txnDate": "%s",
                    "description": "%s"
                }
                """.formatted(userId, categoryId, amount, txnType, txnDate, description);

        // Act & Assert
        mockMvc.perform(
                post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(header().string("Location", "/api/transactions/10"))
        .andExpect(jsonPath("$.transactionId").value(10))
        .andExpect(jsonPath("$.amount").value(75.50))
        .andExpect(jsonPath("$.txnType").value("EXPENSE"));
    }

    
}
