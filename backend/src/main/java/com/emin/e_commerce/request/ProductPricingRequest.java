package com.emin.e_commerce.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductPricingRequest {
    private Long productId;
    private BigDecimal price;
}
