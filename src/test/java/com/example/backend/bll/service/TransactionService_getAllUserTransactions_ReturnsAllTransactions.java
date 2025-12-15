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
class TransactionService_getAllUserTransactions_ReturnsAllTransactions {

    @Mock
    private TransactionInterface transactionPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionPort);
    }

    @Test
    void getAllUserTransactions_ShouldReturnAllTransactions() {
        // Arrange
        Integer userId = 100;
        TransactionDTO transaction1 = new TransactionDTO();
        transaction1.setTransactionId(1);
        transaction1.setUserId(userId);
        
        TransactionDTO transaction2 = new TransactionDTO();
        transaction2.setTransactionId(2);
        transaction2.setUserId(userId);
        
        List<TransactionDTO> expectedTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionPort.findAllByUserId(userId)).thenReturn(expectedTransactions);

        // Act
        List<TransactionDTO> result = transactionService.getAllUserTransactions(userId);

        // Assert
        assertEquals(expectedTransactions.size(), result.size());
        assertEquals(expectedTransactions, result);
        verify(transactionPort, times(1)).findAllByUserId(userId);
    }
}
