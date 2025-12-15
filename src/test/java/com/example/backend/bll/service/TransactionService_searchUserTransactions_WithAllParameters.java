package com.example.backend.bll.service;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.port.out.TransactionInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionService_searchUserTransactions_WithAllParameters {

    @Mock
    private TransactionInterface transactionPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionPort);
    }

    @Test
    void searchUserTransactions_WithAllParameters_ShouldCallPortWithCorrectValues() {
        // Arrange
        Integer userId = 100;
        String description = "groceries";
        Integer categoryId = 2;
        String txnType = "EXPENSE";
        
        TransactionDTO transaction = new TransactionDTO();
        transaction.setTransactionId(1);
        transaction.setUserId(userId);
        transaction.setDescription(description);
        
        List<TransactionDTO> expectedResults = Arrays.asList(transaction);
        
        when(transactionPort.searchTransactions(userId, description, categoryId, txnType))
            .thenReturn(expectedResults);

        // Act
        List<TransactionDTO> result = transactionService.searchUserTransactions(
            userId, description, categoryId, txnType
        );

        // Assert
        assertEquals(expectedResults, result);
        verify(transactionPort, times(1))
            .searchTransactions(userId, description, categoryId, txnType);
    }
}
