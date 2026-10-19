package com.emin.e_commerce.service.productPricing;

import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Product;
import com.emin.e_commerce.model.ProductPricing;
import com.emin.e_commerce.repository.ProductPricingRepository;
import com.emin.e_commerce.repository.ProductRepository;
import com.emin.e_commerce.request.ProductPricingRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPricingServiceImpl implements IProductPricingService {

    private final ProductPricingRepository pricingRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductPricing createPricing(ProductPricingRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + request.getProductId()));

        ProductPricing pricing = new ProductPricing();
        pricing.setProduct(product);
        pricing.setPrice(request.getPrice());

        return pricingRepository.save(pricing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPricing> getPricingsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found with ID: " + productId);
        }
        return pricingRepository.findByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPricing getLatestPrice(Long productId) {
        return pricingRepository.findFirstByProductIdOrderByCreatedDatetimeDesc(productId)
                .orElseThrow(() -> new NotFoundException("No pricing found for Product ID: " + productId));
    }
}
