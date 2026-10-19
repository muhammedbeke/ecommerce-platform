package com.emin.e_commerce.request;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String description;
    private Long parentCategoryId;
}
