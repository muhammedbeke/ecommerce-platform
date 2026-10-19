package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.OrderDto;
import com.emin.e_commerce.request.OrderRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.security.CustomUserDetails;
import com.emin.e_commerce.service.order.IOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllOrders() {
        List<OrderDto> dtos = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse("All orders retrieved successfully!", dtos));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        OrderDto dto = orderService.createOrder(userDetails.getCustomer().getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Order placed successfully!", dto));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse> getMyOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<OrderDto> dtos = orderService.getOrdersByCustomerId(userDetails.getCustomer().getId());
        return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully!", dtos));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderDto dto = orderService.getOrderById(id);
        return ResponseEntity.ok(new ApiResponse("Order retrieved successfully!", dto));
    }

    @PutMapping("/order/{id}/update")
    public ResponseEntity<ApiResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        OrderDto dto = orderService.updateOrder(id, request);
        return ResponseEntity.ok(new ApiResponse("Order updated successfully and status set to PENDING!", dto));
    }

    @DeleteMapping("/order/{id}/delete")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(new ApiResponse("Order deleted successfully!", null));
    }


    @PutMapping("/order/{id}/next-status")
    public ResponseEntity<ApiResponse> advanceStatus(@PathVariable Long id) {
        OrderDto dto = orderService.advanceOrderStatus(id);
        return ResponseEntity.ok(new ApiResponse("Order status advanced successfully!", dto));
    }

    @PutMapping("/order/{id}/roll-back-status")
    public ResponseEntity<ApiResponse> rollBackStatus(@PathVariable Long id) {
        OrderDto dto = orderService.rollbackOrderStatus(id);
        return ResponseEntity.ok(new ApiResponse("Order status roll backed successfully!", dto));
    }

    @PutMapping("/order/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        OrderDto dto = orderService.cancelOrder(id);
        return ResponseEntity.ok(new ApiResponse("Order cancelled successfully!", dto));
    }
}

