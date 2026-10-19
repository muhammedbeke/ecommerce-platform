package com.emin.e_commerce.service.stock;

import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.dto.StockDto;
import com.emin.e_commerce.exception.InsufficientStockException;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Product;
import com.emin.e_commerce.model.Stock;
import com.emin.e_commerce.repository.ProductRepository;
import com.emin.e_commerce.repository.StockRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements IStockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional(readOnly = true)
    public StockDto getStockByProductId(Long productId) {
        return dtoMapper.toStockDto(getStockEntityByProductId(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public Stock getStockEntityByProductId(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Stock entry not found for Product ID: " + productId));
    }

    @Override
    @Transactional
    public StockDto updateStock(Long productId, Integer quantityChange) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
                    Stock newStock = new Stock();
                    newStock.setProduct(product);
                    newStock.setQuantity(0);
                    return stockRepository.save(newStock);
                });

        int newQuantity = stock.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new InsufficientStockException("Insufficient stock for Product ID: " + productId + 
                    ". Available: " + stock.getQuantity() + ", Requested change: " + quantityChange);
        }

        stock.setQuantity(newQuantity);
        Stock updatedStock = stockRepository.save(stock);
        return dtoMapper.toStockDto(updatedStock);
    }

    @Override
    @Transactional
    public StockDto setStock(Long productId, Integer quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative.");
        }

        Stock stock = stockRepository.findByProductId(productId)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new NotFoundException("Product not found with ID: " + productId));
                    Stock newStock = new Stock();
                    newStock.setProduct(product);
                    newStock.setQuantity(0);
                    return newStock;
                });

        stock.setQuantity(quantity);
        Stock updatedStock = stockRepository.save(stock);
        return dtoMapper.toStockDto(updatedStock);
    }
}
