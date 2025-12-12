package com.example.backend.bll.service;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.port.out.CategoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryService_getByName_FoundReturnsCategory {

    @Mock
    private CategoryInterface categoryRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    void getByName_foundReturnsCategory() {
        CategoryDTO salary = new CategoryDTO(1, "Salary", true);
        when(categoryRepository.findByName("Salary")).thenReturn(Optional.of(salary));

        Optional<CategoryDTO> result = categoryService.getByName("Salary");

        assertTrue(result.isPresent());
        assertEquals("Salary", result.get().getName());
        verify(categoryRepository, times(1)).findByName("Salary");
    }
}
