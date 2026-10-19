package com.emin.e_commerce.service.cart;

import com.emin.e_commerce.dto.CartDto;
import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.*;
import com.emin.e_commerce.repository.CartItemRepository;
import com.emin.e_commerce.repository.CartRepository;
import com.emin.e_commerce.service.customer.ICustomerService;
import com.emin.e_commerce.service.product.IProductService;
import com.emin.e_commerce.service.productPricing.IProductPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ICustomerService customerService;
    private final IProductService productService;
    private final IProductPricingService productPricingService;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public CartDto getCartByCustomerId(Long customerId) {
        return dtoMapper.toCartDto(getCartEntityByCustomerId(customerId));
    }

    @Override
    @Transactional
    public Cart getCartEntityByCustomerId(Long customerId) {
        Customer customer = customerService.getCustomerEntityById(customerId);
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    cart.setTotalAmount(BigDecimal.ZERO);
                    return cartRepository.save(cart);
                });
    }

    @Override
    @Transactional
    public CartDto addProductToCart(Long customerId, Long productId, Integer quantity) {
        Cart cart = getCartEntityByCustomerId(customerId);
        Product product = productService.getProductEntityById(productId);
        ProductPricing pricing = productPricingService.getLatestPrice(productId);

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setUnitPrice(pricing.getPrice());
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setUnitPrice(pricing.getPrice());
            item.setTotalPrice(pricing.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getCartItems().add(item);
        }

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        System.out.println("DEBUG - addProductToCart - Customer ID: " + customerId);
        System.out.println("DEBUG - addProductToCart - Cart ID: " + savedCart.getId());
        System.out.println("DEBUG - addProductToCart - Saved Cart Items Size: " + savedCart.getCartItems().size());
        return dtoMapper.toCartDto(savedCart);
    }


    @Override
    @Transactional
    public CartDto updateCartItemQuantity(Long customerId, Long productId, Integer quantity) {
        Cart cart = getCartEntityByCustomerId(customerId);

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found in cart: " + productId));

        if (quantity <= 0) {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        }

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return dtoMapper.toCartDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto removeProductFromCart(Long customerId, Long productId) {
        Cart cart = getCartEntityByCustomerId(customerId);

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found in cart: " + productId));

        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);

        recalculateCartTotal(cart);
        Cart savedCart = cartRepository.save(cart);
        return dtoMapper.toCartDto(savedCart);
    }

    @Override
    @Transactional
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDto clearAllCart(Long customerId) {
        Cart cart = getCartEntityByCustomerId(customerId);
        clearCart(cart);
        return dtoMapper.toCartDto(cart);
    }

    private void recalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }
}
