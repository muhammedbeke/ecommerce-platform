package com.emin.e_commerce.dto;

import lombok.Data;

@Data
public class StockDto {
    private Long id;
    private Long productId;
    private int quantity;
}
