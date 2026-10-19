package com.emin.e_commerce.service.order;

import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.dto.OrderDto;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.*;
import com.emin.e_commerce.repository.OrderRepository;
import com.emin.e_commerce.service.customer.ICustomerService;
import com.emin.e_commerce.service.ListOfValues.IListOfValuesService;
import com.emin.e_commerce.service.cart.ICartService;
import com.emin.e_commerce.service.product.IProductService;
import com.emin.e_commerce.service.stock.IStockService;
import com.emin.e_commerce.request.OrderRequest;
import com.emin.e_commerce.request.OrderItemRequest;
import com.emin.e_commerce.dto.ProductDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final ICustomerService customerService;
    private final IStockService stockService;
    private final ICartService cartService;
    private final DtoMapper dtoMapper;
    private final IListOfValuesService listOfValuesService;
    private final IProductService productService;

    @Override
    @Transactional
    public OrderDto createOrder(Long id) {
        Customer customer = customerService.getCustomerEntityById(id);
        if (customer.getAddresses() == null || customer.getAddresses().isEmpty()) {
            throw new NotFoundException("No address for Customer ID: " + customer.getId());
        }

        Cart cart = cartService.getCartEntityByCustomerId(id);
        System.out.println("DEBUG - Customer ID: " + id);
        System.out.println("DEBUG - Cart ID: " + (cart != null ? cart.getId() : "null"));
        System.out.println("DEBUG - Cart Items Size: " + (cart != null && cart.getCartItems() != null ? cart.getCartItems().size() : "null"));
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().forEach(item -> {
                System.out.println("  DEBUG - Item ID: " + item.getId() + ", Product ID: " + (item.getProduct() != null ? item.getProduct().getId() : "null") + ", Quantity: " + item.getQuantity());
            });
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot place order: Cart is empty!");
        }


        ListOfValues pendingStatus = listOfValuesService.getByTypeAndCode("ORDER_STATUS", 
        "PENDING");

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(pendingStatus);

        for (CartItem item : cart.getCartItems()) {
            Product product = item.getProduct();

            stockService.updateStock(product.getId(), -item.getQuantity());

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setPrice(item.getUnitPrice());

            order.getOrderDetails().add(orderDetail);
        }

        cartService.clearCart(cart);

        Order savedOrder = orderRepository.save(order);
        return dtoMapper.toOrderDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long id) {
        return dtoMapper.toOrderDto(getOrderEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntityById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        customerService.getCustomerEntityById(customerId);
        return orderRepository.findByCustomerId(customerId).stream()
                .map(dtoMapper::toOrderDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderEntityById(id);
        orderRepository.delete(order);
    }

    @Override
    @Transactional
    public OrderDto advanceOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found! ID: " + orderId));

        String currentStatusCode = order.getOrderStatus().getCode();

        String nextStatusCode = switch (currentStatusCode) {
            case "PENDING" -> "PROCESSING";
            case "PROCESSING" -> "SHIPPED";
            case "SHIPPED" -> "DELIVERED";
            case "DELIVERED" -> throw new IllegalArgumentException("Order has already been delivered and cannot be advanced further!");
            case "CANCELLED" -> throw new IllegalArgumentException("Cancelled order status cannot be advanced!");
            default -> throw new IllegalArgumentException("Unknown order status: " + currentStatusCode);
        };

        ListOfValues nextStatusLov = listOfValuesService.getByTypeAndCode("ORDER_STATUS", nextStatusCode);
        order.setOrderStatus(nextStatusLov);
        Order updatedOrder = orderRepository.save(order);

        return dtoMapper.toOrderDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDto rollbackOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found! ID: " + orderId));

        String currentStatusCode = order.getOrderStatus().getCode();

     String previousStatusCode = switch (currentStatusCode) {
        case "DELIVERED" -> "SHIPPED";
        case "SHIPPED" -> "PROCESSING";
        case "PROCESSING" -> "PENDING";
        case "CANCELLED" -> "PENDING"; 
        case "PENDING" -> throw new IllegalArgumentException("Order is already in PENDING status and cannot be rolled back further!");
        default -> throw new IllegalArgumentException("Unknown order status: " + currentStatusCode);
    };

        ListOfValues previousStatusLov = listOfValuesService.getByTypeAndCode("ORDER_STATUS", previousStatusCode);
        order.setOrderStatus(previousStatusLov);
        Order updatedOrder = orderRepository.save(order);

        return dtoMapper.toOrderDto(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found! ID: " + orderId));

        String currentStatusCode = order.getOrderStatus().getCode();

        if ("DELIVERED".equals(currentStatusCode)) {
            throw new IllegalArgumentException("Delivered order cannot be cancelled!");
        }
        if ("SHIPPED".equals(currentStatusCode)) {
            throw new IllegalArgumentException("Shipped order cannot be cancelled!");
        }
        if ("CANCELLED".equals(currentStatusCode)) {
            throw new IllegalArgumentException("Order is already cancelled!");
        }

        ListOfValues cancelledStatusLov = listOfValuesService.getByTypeAndCode("ORDER_STATUS", "CANCELLED");
        order.getOrderDetails().forEach(detail -> {
            stockService.updateStock(detail.getProduct().getId(), detail.getQuantity());
        });

        order.setOrderStatus(cancelledStatusLov);
        Order updatedOrder = orderRepository.save(order);

        return dtoMapper.toOrderDto(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(dtoMapper::toOrderDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto updateOrder(Long id, OrderRequest request) {
        Order order = getOrderEntityById(id);

        if (request.getItems() != null) {
            for (OrderDetail oldDetail : order.getOrderDetails()) {
                if (oldDetail.getProduct() != null) {
                    stockService.updateStock(oldDetail.getProduct().getId(), oldDetail.getQuantity());
                }
            }
            order.getOrderDetails().clear();

            for (OrderItemRequest itemReq : request.getItems()) {
                if (itemReq.getProductId() == null || itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                    continue;
                }
                Product product = productService.getProductEntityById(itemReq.getProductId());

                stockService.updateStock(product.getId(), -itemReq.getQuantity());

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQuantity(itemReq.getQuantity());
                if (itemReq.getPrice() != null) {
                    detail.setPrice(itemReq.getPrice());
                } else {
                    ProductDto productDto = dtoMapper.toProductDto(product);
                    detail.setPrice(productDto != null && productDto.getPrice() != null ? productDto.getPrice() : java.math.BigDecimal.ZERO);
                }
                order.getOrderDetails().add(detail);
            }
        }

        ListOfValues pendingStatus = listOfValuesService.getByTypeAndCode("ORDER_STATUS", "PENDING");
        order.setOrderStatus(pendingStatus);

        Order savedOrder = orderRepository.save(order);
        return dtoMapper.toOrderDto(savedOrder);
    }
}
