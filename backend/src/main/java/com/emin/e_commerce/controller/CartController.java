package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.CartDto;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.security.CustomUserDetails;
import com.emin.e_commerce.service.cart.ICartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse> getMyCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        CartDto dto = cartService.getCartByCustomerId(userDetails.getCustomer().getId());
        return ResponseEntity.ok(new ApiResponse("Cart retrieved successfully!", dto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProductToCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @RequestParam Long productId,
                                                         @RequestParam Integer quantity) {
        CartDto dto = cartService.addProductToCart(userDetails.getCustomer().getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Product added to cart successfully!", dto));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCartItemQuantity(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestParam Long productId,
                                                               @RequestParam Integer quantity) {
        CartDto dto = cartService.updateCartItemQuantity(userDetails.getCustomer().getId(), productId, quantity);
        return ResponseEntity.ok(new ApiResponse("Cart item quantity updated successfully!", dto));
    }

    @DeleteMapping("/remove/product")
    public ResponseEntity<ApiResponse> removeProductFromCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestParam Long productId) {
        CartDto dto = cartService.removeProductFromCart(userDetails.getCustomer().getId(), productId);
        return ResponseEntity.ok(new ApiResponse("Product removed from cart successfully!", dto));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearAllCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        CartDto dto = cartService.clearAllCart(userDetails.getCustomer().getId());
        return ResponseEntity.ok(new ApiResponse("Cart cleared successfully!", dto));
    }
}
