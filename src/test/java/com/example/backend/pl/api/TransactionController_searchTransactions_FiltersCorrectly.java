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
class TransactionController_searchTransactions_FiltersCorrectly {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void searchTransactions_WithAllFilters_ShouldReturn200WithFilteredResults() throws Exception {
        // Arrange
        Integer userId = 1;
        String description = "grocery";
        Integer categoryId = 5;
        String txnType = "EXPENSE";

        TransactionDTO tx1 = new TransactionDTO(1, userId, "Weekly groceries", categoryId, txnType, new BigDecimal("75.50"), LocalDate.now(), null);
        List<TransactionDTO> results = Arrays.asList(tx1);

        when(transactionService.searchUserTransactions(userId, description, categoryId, txnType))
                .thenReturn(results);

        // Act & Assert
        mockMvc.perform(
                get("/api/transactions/search")
                        .param("userId", userId.toString())
                        .param("description", description)
                        .param("categoryId", categoryId.toString())
                        .param("txnType", txnType)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].description").value("Weekly groceries"))
        .andExpect(jsonPath("$[0].txnType").value("EXPENSE"));
    }

    
}
