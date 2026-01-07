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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionController_getUserTransactions_ReturnsOkWithTransactions {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void getUserTransactions_WithValidUserId_ShouldReturn200WithTransactions() throws Exception {
        // Arrange
        Integer userId = 1;
        TransactionDTO tx1 = new TransactionDTO(1, userId, "Groceries", 1, "EXPENSE", new BigDecimal("50.00"), LocalDate.now(), null);
        TransactionDTO tx2 = new TransactionDTO(2, userId, "Salary", 2, "INCOME", new BigDecimal("1500.00"), LocalDate.now(), null);
        List<TransactionDTO> transactions = Arrays.asList(tx1, tx2);

        when(transactionService.getAllUserTransactions(userId)).thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(
                get("/api/transactions")
                        .param("userId", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].transactionId").value(1))
        .andExpect(jsonPath("$[0].amount").value(50.00))
        .andExpect(jsonPath("$[0].txnType").value("EXPENSE"))
        .andExpect(jsonPath("$[1].transactionId").value(2))
        .andExpect(jsonPath("$[1].amount").value(1500.00))
        .andExpect(jsonPath("$[1].txnType").value("INCOME"));
    }
}
