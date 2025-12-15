package com.example.backend.bll.service;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.port.out.TransactionInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionService_save_ReturnsSavedTransaction {

    @Mock
    private TransactionInterface transactionPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionPort);
    }

    @Test
    void save_ShouldReturnSavedTransaction() {
        // Arrange
        TransactionDTO transaction = new TransactionDTO();
        transaction.setTransactionId(1);
        transaction.setUserId(100);
        transaction.setDescription("Test transaction");
        
        when(transactionPort.save(transaction)).thenReturn(transaction);

        // Act
        TransactionDTO result = transactionService.save(transaction);

        // Assert
        assertNotNull(result);
        assertEquals(transaction, result);
        verify(transactionPort, times(1)).save(transaction);
    }
}
