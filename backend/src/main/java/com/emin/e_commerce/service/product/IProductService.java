package com.emin.e_commerce.service.product;

import com.emin.e_commerce.dto.ProductDto;
import com.emin.e_commerce.model.Product;
import com.emin.e_commerce.request.ProductRequest;

import java.util.List;

public interface IProductService {
    ProductDto createProduct(ProductRequest request);
    ProductDto updateProduct(Long id, ProductRequest request);
    ProductDto getProductById(Long id);
    Product getProductEntityById(Long id);
    List<ProductDto> getAllProducts();
    void deleteProduct(Long id);
    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);
}
