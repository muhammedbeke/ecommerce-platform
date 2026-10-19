package com.emin.e_commerce.service.cart;

import com.emin.e_commerce.dto.CartDto;
import com.emin.e_commerce.model.Cart;

public interface ICartService {
    CartDto getCartByCustomerId(Long customerId);
    Cart getCartEntityByCustomerId(Long customerId);
    CartDto addProductToCart(Long customerId, Long productId, Integer quantity);
    CartDto updateCartItemQuantity(Long customerId, Long productId, Integer quantity);
    CartDto removeProductFromCart(Long customerId, Long productId);
    void clearCart(Cart cart);
    CartDto clearAllCart(Long customerId);
}
