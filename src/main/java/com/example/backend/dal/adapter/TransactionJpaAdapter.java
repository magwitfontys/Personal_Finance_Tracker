package com.example.backend.dal.adapter;

import com.example.backend.bll.dto.TransactionDTO;
import com.example.backend.bll.port.out.TransactionInterface;
import com.example.backend.dal.entity.TransactionEntity;
import com.example.backend.dal.repo.TransactionJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionJpaAdapter implements TransactionInterface {

    private final TransactionJpaRepository repo;

    public TransactionJpaAdapter(TransactionJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserId(Integer userId) {
        return repo.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> findAllByUserId(Integer userId) {
        return repo.findByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> searchTransactions(
            Integer userId,
            String description,
            Integer categoryId,
            String txnType
    ) {
        if (description == null) {
            description = "";
        }

        return repo
                .findByUserIdAndDescriptionContainingIgnoreCaseAndCategoryIdAndTxnType(
                        userId,
                        description,
                        categoryId,
                        txnType
                )
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionDTO> findById(Integer transactionId) {
        return repo.findById(transactionId).map(this::toDTO);
    }

    @Override
    @Transactional
    public TransactionDTO save(TransactionDTO dto) {
        TransactionEntity entity;

        // If id present, try to update, otherwise create new
        if (dto.getTransactionId() != null) {
            entity = repo.findById(dto.getTransactionId())
                    .orElseGet(TransactionEntity::new);
        } else {
            entity = new TransactionEntity();
        }

        entity.setUserId(dto.getUserId());
        entity.setCategoryId(dto.getCategoryId());
        entity.setAmount(dto.getAmount());
        entity.setDescription(dto.getDescription());
        entity.setTxnType(dto.getTxnType());
        entity.setTxnDate(dto.getTxnDate());
        // createdAt is handled by the database (insertable = false, updatable = false)

        TransactionEntity saved = repo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteById(Integer transactionId) {
        repo.deleteById(transactionId);
    }

    // =========================
    // Mapper helpers
    // =========================

    private TransactionDTO toDTO(TransactionEntity e) {
        if (e == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(e.getTransactionId());
        dto.setUserId(e.getUserId());
        dto.setCategoryId(e.getCategoryId());
        dto.setAmount(e.getAmount());
        dto.setDescription(e.getDescription());
        dto.setTxnType(e.getTxnType());
        dto.setTxnDate(e.getTxnDate());
        dto.setCreatedAt(e.getCreatedAt());

        return dto;
    }
}
