package com.example.backend.bll.service;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.port.out.CategoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryService_getOutcomeNames_ReturnsNamesOnly {

    @Mock
    private CategoryInterface categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getOutcomeNames_returnsNamesOnly() {
        CategoryDTO groceries = new CategoryDTO(1, "Groceries", false);
        CategoryDTO utilities = new CategoryDTO(2, "Utilities", false);
        CategoryDTO salary = new CategoryDTO(3, "Salary", true); // opposite type
        List<CategoryDTO> mixed = Arrays.asList(groceries, utilities, salary);

        when(categoryRepository.findByIncome(false)).thenReturn(mixed);

        List<String> result = categoryService.getOutcomeNames();

        assertEquals(2, result.size());
        assertTrue(result.contains("Groceries"));
        assertTrue(result.contains("Utilities"));
        assertFalse(result.contains("Salary"));
    }
}
