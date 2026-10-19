package com.emin.e_commerce.service.productPricing;

import com.emin.e_commerce.model.ProductPricing;
import com.emin.e_commerce.request.ProductPricingRequest;

import java.util.List;

public interface IProductPricingService {
    ProductPricing createPricing(ProductPricingRequest request);
    List<ProductPricing> getPricingsByProductId(Long productId);
    ProductPricing getLatestPrice(Long productId);
}
