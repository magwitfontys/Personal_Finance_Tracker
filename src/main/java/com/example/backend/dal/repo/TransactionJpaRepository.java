package com.example.backend.dal.repo;

import com.example.backend.dal.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Integer> {

    // counting how many transactions this user has (for stats if needed)
    long countByUserId(Integer userId);

    // get all transactions for one user (no filters) => for transactions main page
    List<TransactionEntity> findByUserId(Integer userId);

    // search by user + description text + category + type => for transation page with filters
    List<TransactionEntity> findByUserIdAndDescriptionContainingIgnoreCaseAndCategoryIdAndTxnType(
            Integer userId,
            String description,
            Integer categoryId,
            String txnType
    );

    // delete all transactions for a user
    void deleteByUserId(Integer userId);
}
