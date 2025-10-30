package com.example.backend.bll.port.out;

import com.example.backend.bll.dto.CategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryInterface {
    Optional<CategoryDTO> findByName(String name);

    List<CategoryDTO> findAll();

    List<CategoryDTO> findByIncome(boolean isIncome);
}
