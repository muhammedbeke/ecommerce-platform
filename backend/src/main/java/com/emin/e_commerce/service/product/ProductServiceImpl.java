package com.emin.e_commerce.service.product;

import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.dto.ProductDto;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.*;
import com.emin.e_commerce.repository.ProductPricingRepository;
import com.emin.e_commerce.repository.ProductRepository;
import com.emin.e_commerce.repository.StockRepository;
import com.emin.e_commerce.request.ProductRequest;
import com.emin.e_commerce.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ICategoryService categoryService;
    private final ProductPricingRepository pricingRepository;
    private final StockRepository stockRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public ProductDto createProduct(ProductRequest request) {
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryService.getCategoryEntityById(request.getCategoryId());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        if (request.getLowStockThreshold() != null) {
            product.setLowStockThreshold(request.getLowStockThreshold());
        }
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        BigDecimal initialPrice = request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO;
        ProductPricing pricing = new ProductPricing();
        pricing.setProduct(savedProduct);
        pricing.setPrice(initialPrice);
        pricingRepository.save(pricing);

        Stock stock = new Stock();
        stock.setProduct(savedProduct);
        stock.setQuantity(0);
        stockRepository.save(stock);

        return dtoMapper.toProductDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductRequest request) {
        Product product = getProductEntityById(id);

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getBrand() != null) {
            product.setBrand(request.getBrand());
        }
        if (request.getLowStockThreshold() != null) {
            product.setLowStockThreshold(request.getLowStockThreshold());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryService.getCategoryEntityById(request.getCategoryId());
            product.setCategory(category);
        }

        Product savedProduct = productRepository.save(product);

        if (request.getPrice() != null) {
            BigDecimal currentPrice = pricingRepository.findFirstByProductIdOrderByCreatedDatetimeDesc(id)
                    .map(ProductPricing::getPrice)
                    .orElse(null);

            if (currentPrice == null || currentPrice.compareTo(request.getPrice()) != 0) {
                ProductPricing pricing = new ProductPricing();
                pricing.setProduct(savedProduct);
                pricing.setPrice(request.getPrice());
                pricingRepository.save(pricing);
            }
        }

        return dtoMapper.toProductDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        return dtoMapper.toProductDto(getProductEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(dtoMapper::toProductDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductEntityById(id);
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        return dtoMapper.toProductDto(product);
    }

}
