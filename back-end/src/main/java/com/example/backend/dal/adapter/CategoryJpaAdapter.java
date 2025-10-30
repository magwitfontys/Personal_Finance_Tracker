package com.example.backend.dal.adapter;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.port.out.CategoryInterface;
import com.example.backend.dal.entity.CategoryEntity;
import com.example.backend.dal.repo.CategoryJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryJpaAdapter implements CategoryInterface {

    private final CategoryJpaRepository repo;

    public CategoryJpaAdapter(CategoryJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoryDTO> findByName(String name) {
        return repo.findByName(name).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return repo.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findByIncome(boolean income) {
        // Repository uses the entity property name `isIncome`
        return repo.findByIsIncome(income).stream()
                .map(this::toDTO)
                .toList();
    }

    // Mapper
    private CategoryDTO toDTO(CategoryEntity e) {
        return new CategoryDTO(e.getCategoryId(), e.getName(), e.isIncome());
    }
}
