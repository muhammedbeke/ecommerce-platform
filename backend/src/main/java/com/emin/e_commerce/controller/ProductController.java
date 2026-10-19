package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.ProductDto;
import com.emin.e_commerce.request.ProductRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.product.IProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<ProductDto> dtos = productService.getAllProducts();
        return ResponseEntity.ok(new ApiResponse("All products retrieved successfully!", dtos));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductDto dto = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse("Product retrieved successfully!", dto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody ProductRequest request) {
        ProductDto dto = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Product created successfully!", dto));
    }

    @PutMapping("/product/{id}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        ProductDto dto = productService.updateProduct(id, request);
        return ResponseEntity.ok(new ApiResponse("Product updated successfully!", dto));
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully!", null));
    }
}
