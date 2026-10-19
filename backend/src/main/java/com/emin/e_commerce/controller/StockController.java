package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.StockDto;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.stock.IStockService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {

    private final IStockService stockService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getStockByProductId(@PathVariable Long productId) {
        StockDto dto = stockService.getStockByProductId(productId);
        return ResponseEntity.ok(new ApiResponse("Stock retrieved successfully!", dto));
    }

    @PostMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateStock(@PathVariable Long productId, @RequestParam Integer quantityChange) {
        StockDto dto = stockService.updateStock(productId, quantityChange);
        return ResponseEntity.ok(new ApiResponse("Stock updated successfully!", dto));
    }

    @PostMapping("/product/{productId}/set")
    public ResponseEntity<ApiResponse> setStock(@PathVariable Long productId, @RequestParam Integer quantity) {
        StockDto dto = stockService.setStock(productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Stock set successfully!", dto));
    }
}
