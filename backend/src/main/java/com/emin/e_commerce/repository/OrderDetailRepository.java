package com.emin.e_commerce.repository;

import com.emin.e_commerce.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
