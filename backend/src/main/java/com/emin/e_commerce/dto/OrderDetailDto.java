package com.emin.e_commerce.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderDetailDto {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
