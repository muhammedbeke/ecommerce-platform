package com.emin.e_commerce.repository;

import com.emin.e_commerce.model.BuyingDetail;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BuyingDetailRepository extends JpaRepository<BuyingDetail, Long> {
    List<BuyingDetail> findByBuyingId(Long buyingId);
}
