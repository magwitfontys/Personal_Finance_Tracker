package com.example.backend.dal.repo;

import com.example.backend.dal.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findByName(String name);

    List<CategoryEntity> findByIsIncome(boolean income);

}
