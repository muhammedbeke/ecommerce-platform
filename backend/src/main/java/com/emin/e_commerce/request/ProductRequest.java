package com.emin.e_commerce.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String brand;
    private Long categoryId;
    private BigDecimal price; 
    private Integer lowStockThreshold;
}
