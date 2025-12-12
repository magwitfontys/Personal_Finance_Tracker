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
class CategoryService_getIncomeNames_ReturnsNamesOnly {

    @Mock
    private CategoryInterface categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getIncomeNames_returnsNamesOnly() {
        CategoryDTO salary = new CategoryDTO(1, "Salary", true);
        CategoryDTO bonus = new CategoryDTO(2, "Bonus", true);
        List<CategoryDTO> income = Arrays.asList(salary, bonus);

        when(categoryRepository.findByIncome(true)).thenReturn(income);

        List<String> result = categoryService.getIncomeNames();

        assertEquals(2, result.size());
        assertTrue(result.contains("Salary"));
        assertTrue(result.contains("Bonus"));
    }
}
