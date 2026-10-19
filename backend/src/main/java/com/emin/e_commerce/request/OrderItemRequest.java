package com.emin.e_commerce.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}
