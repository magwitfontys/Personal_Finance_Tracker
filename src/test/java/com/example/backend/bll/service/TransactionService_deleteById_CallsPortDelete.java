package com.example.backend.bll.service;

import com.example.backend.bll.port.out.TransactionInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionService_deleteById_CallsPortDelete {

    @Mock
    private TransactionInterface transactionPort;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionPort);
    }

    @Test
    void deleteById_ShouldCallPortDeleteMethod() {
        // Arrange
        Integer transactionId = 1;

        // Act
        transactionService.deleteById(transactionId);

        // Assert
        verify(transactionPort, times(1)).deleteById(transactionId);
    }
}
