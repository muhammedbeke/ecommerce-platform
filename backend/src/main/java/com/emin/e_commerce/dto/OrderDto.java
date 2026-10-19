package com.emin.e_commerce.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long customerId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;
}
