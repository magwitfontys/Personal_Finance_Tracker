package com.example.backend.bll.service;

import com.example.backend.bll.port.out.TransactionInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionService_countUserTransactions_ReturnsCount {

    @Mock
    private TransactionInterface transactionPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionPort);
    }

    @Test
    void countUserTransactions_ShouldReturnCount() {
        // Arrange
        Integer userId = 100;
        long expectedCount = 5L;
        when(transactionPort.countByUserId(userId)).thenReturn(expectedCount);

        // Act
        long result = transactionService.countUserTransactions(userId);

        // Assert
        assertEquals(expectedCount, result);
        verify(transactionPort, times(1)).countByUserId(userId);
    }
}
