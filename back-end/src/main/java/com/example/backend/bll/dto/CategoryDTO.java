package com.example.backend.bll.dto;

public class CategoryDTO {
    private Integer id;
    private String name;
    private boolean isIncome;

    public CategoryDTO() {
    }

    public CategoryDTO(Integer id, String name, boolean isIncome) {
        this.id = id;
        this.name = name;
        this.isIncome = isIncome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public void setIncome(boolean income) {
        isIncome = income;
    }

}
