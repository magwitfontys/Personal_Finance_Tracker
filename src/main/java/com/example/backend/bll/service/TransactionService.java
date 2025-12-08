package com.example.backend.bll.service;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.port.out.TransactionInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionInterface transactionPort;

    public TransactionService(TransactionInterface transactionPort) {
        this.transactionPort = transactionPort;
    }

    public long countUserTransactions(Integer userId) {
        return transactionPort.countByUserId(userId);
    }

    public List<TransactionDTO> getAllUserTransactions(Integer userId) {
        return transactionPort.findAllByUserId(userId);
    }

    public List<TransactionDTO> searchUserTransactions(
            Integer userId,
            String description,
            Integer categoryId,
            String txnType
    ) {
        // desc can be null, if so it has value of ""
        if (description == null) {
            description = "";
        }

        return transactionPort.searchTransactions(userId, description, categoryId, txnType);
    }
    
    public TransactionDTO save(TransactionDTO transaction) {
        return transactionPort.save(transaction);
    }

    public void deleteById(Integer transactionId) {
        transactionPort.deleteById(transactionId);
    }

    public void deleteAllByUserId(Integer userId) {
        transactionPort.deleteAllByUserId(userId);
    }
}
