package com.emin.e_commerce.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String brand;
    private Integer lowStockThreshold;
    private CategoryDto category;
    private List<ImageDto> images;
}
