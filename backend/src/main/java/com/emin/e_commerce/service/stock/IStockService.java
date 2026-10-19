package com.emin.e_commerce.service.stock;

import com.emin.e_commerce.dto.StockDto;
import com.emin.e_commerce.model.Stock;

public interface IStockService {
    StockDto getStockByProductId(Long productId);
    Stock getStockEntityByProductId(Long productId);
    StockDto updateStock(Long productId, Integer quantityChange);
    StockDto setStock(Long productId, Integer quantity);
}
