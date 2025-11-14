package com.example.backend.pl.api;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // All categories => for drowpdowns lists
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAll();
        return ResponseEntity.ok(categories);
    }

    // Income categories => for dropdown lists for income
    @GetMapping("/income")
    public ResponseEntity<List<CategoryDTO>> getIncomeCategories() {
        List<CategoryDTO> income = categoryService.getIncomeCategories();
        return ResponseEntity.ok(income);
    }

    // Outcome categories => for dropdown lists for outcome
    @GetMapping("/outcome")
    public ResponseEntity<List<CategoryDTO>> getOutcomeCategories() {
        List<CategoryDTO> outcome = categoryService.getOutcomeCategories();
        return ResponseEntity.ok(outcome);
    }
}
