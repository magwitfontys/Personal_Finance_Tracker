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
class CategoryServiceTest {

    @Mock
    private CategoryInterface categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        CategoryDTO salary = new CategoryDTO(1, "Salary", true);
        CategoryDTO groceries = new CategoryDTO(2, "Groceries", false);
        List<CategoryDTO> mockCategories = Arrays.asList(salary, groceries);
        
        when(categoryRepository.findAll()).thenReturn(mockCategories);

        // Act
        List<CategoryDTO> result = categoryService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Salary", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}
