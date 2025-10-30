package com.example.backend.bll.service;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.port.out.CategoryInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryInterface categories;

    public CategoryService(CategoryInterface categories) {
        this.categories = categories;
    }

    public List<CategoryDTO> getAll() {
        return categories.findAll();
    }

    public List<CategoryDTO> getIncomeCategories() {
        return categories.findByIncome(true);
    }

    public List<CategoryDTO> getOutcomeCategories() {
        return categories.findByIncome(false);
    }

    public Optional<CategoryDTO> getByName(String name) {
        return categories.findByName(name);
    }

    // Optional helpers if you only need names:
    public List<String> getIncomeNames() {
        return getIncomeCategories().stream().map(CategoryDTO::getName).toList();
    }

    public List<String> getOutcomeNames() {
        return getOutcomeCategories().stream().map(CategoryDTO::getName).toList();
    }
}
