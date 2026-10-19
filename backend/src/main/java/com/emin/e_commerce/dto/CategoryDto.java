package com.emin.e_commerce.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private Long parentCategoryId;
}
