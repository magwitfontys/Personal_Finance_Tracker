package com.example.backend.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories", indexes = {
        @Index(name = "ix_categories_id", columnList = "category_id", unique = true),
        @Index(name = "ix_categories_name", columnList = "name"),
        @Index(name = "ix_categories_is_income", columnList = "is_income")
})

public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false, updatable = false)
    private Integer categoryId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "is_income", nullable = false)
    private Boolean isIncome;

    public CategoryEntity() {
    }

    public CategoryEntity(Integer categoryId, String name, Boolean isIncome) {
        this.categoryId = categoryId;
        this.name = name;
        this.isIncome = isIncome;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public boolean isIncome() {
        return Boolean.TRUE.equals(this.isIncome);
    }

    public boolean isOutcome() {
        return Boolean.FALSE.equals(this.isIncome);
    }

    public boolean getIsIncome() {
        return isIncome();
    }

    public boolean getIsOutcome() {
        return isOutcome();
    }

}
