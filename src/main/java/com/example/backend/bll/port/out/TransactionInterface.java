package com.example.backend.bll.port.out;

import com.example.backend.bll.dto.TransactionDTO;

import java.util.List;
import java.util.Optional;

public interface TransactionInterface {
    Optional<TransactionDTO> findById(Integer id);

    long countByUserId(Integer userId); 

    List<TransactionDTO> findAllByUserId(Integer userId);

    List<TransactionDTO> searchTransactions(
            Integer userId,
            String description,
            Integer categoryId,
            String txnType
    );
    

    TransactionDTO save(TransactionDTO transaction);
    
    void deleteById(Integer id);

    void deleteAllByUserId(Integer userId);

}
