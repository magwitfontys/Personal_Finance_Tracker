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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionController_updateTransaction_ValidRequestReturnsOk {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void updateTransaction_WithValidData_ShouldReturn200WithUpdatedTransaction() throws Exception {
        // Arrange
        Integer transactionId = 5;
        Integer userId = 1;
        Integer categoryId = 3;
        BigDecimal newAmount = new BigDecimal("100.00");
        String txnType = "EXPENSE";
        LocalDate txnDate = LocalDate.now();
        String newDescription = "Updated groceries";

        TransactionDTO updatedTx = new TransactionDTO(transactionId, userId, newDescription, categoryId, txnType, newAmount, txnDate, null);

        when(transactionService.save(any(TransactionDTO.class))).thenReturn(updatedTx);

        String requestBody = """
                {
                    "userId": %d,
                    "categoryId": %d,
                    "amount": %.2f,
                    "txnType": "%s",
                    "txnDate": "%s",
                    "description": "%s"
                }
                """.formatted(userId, categoryId, newAmount, txnType, txnDate, newDescription);

        // Act & Assert
        mockMvc.perform(
                put("/api/transactions/" + transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.transactionId").value(transactionId))
        .andExpect(jsonPath("$.amount").value(100.00))
        .andExpect(jsonPath("$.description").value("Updated groceries"));
    }

    
}
