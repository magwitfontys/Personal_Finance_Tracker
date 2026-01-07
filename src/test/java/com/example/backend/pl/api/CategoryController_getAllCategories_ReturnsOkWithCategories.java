package com.example.backend.pl.api;

import com.example.backend.bll.dto.CategoryDTO;
import com.example.backend.bll.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryController_getAllCategories_ReturnsOkWithCategories {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_ShouldReturn200WithAllCategories() throws Exception {
        // Arrange
        CategoryDTO cat1 = new CategoryDTO(1, "Groceries", false);
        CategoryDTO cat2 = new CategoryDTO(2, "Salary", true);
        CategoryDTO cat3 = new CategoryDTO(3, "Entertainment", false);
        List<CategoryDTO> categories = Arrays.asList(cat1, cat2, cat3);

        when(categoryService.getAll()).thenReturn(categories);

        // Act & Assert
        mockMvc.perform(
                get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Groceries"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].name").value("Salary"))
        .andExpect(jsonPath("$[2].id").value(3));
    }

    
}
