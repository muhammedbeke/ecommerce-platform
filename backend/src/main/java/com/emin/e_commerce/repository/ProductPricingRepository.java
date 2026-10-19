package com.emin.e_commerce.repository;

import com.emin.e_commerce.model.ProductPricing;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface ProductPricingRepository extends JpaRepository<ProductPricing, Long> {
    List<ProductPricing> findByProductId(Long productId);
    Optional<ProductPricing> findFirstByProductIdOrderByCreatedDatetimeDesc(Long productId);
}
