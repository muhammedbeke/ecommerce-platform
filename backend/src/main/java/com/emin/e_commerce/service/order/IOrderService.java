package com.emin.e_commerce.service.order;

import com.emin.e_commerce.dto.OrderDto;
import com.emin.e_commerce.model.Order;
import com.emin.e_commerce.request.OrderRequest;

import java.util.List;

public interface IOrderService {
    OrderDto createOrder(Long id);
    OrderDto getOrderById(Long id);
    Order getOrderEntityById(Long id);
    List<OrderDto> getOrdersByCustomerId(Long customerId);
    List<OrderDto> getAllOrders();
    void deleteOrder(Long id);
    OrderDto advanceOrderStatus(Long id);
    OrderDto cancelOrder(Long id);
    OrderDto rollbackOrderStatus(Long orderId);
    OrderDto updateOrder(Long id, OrderRequest request);
}

