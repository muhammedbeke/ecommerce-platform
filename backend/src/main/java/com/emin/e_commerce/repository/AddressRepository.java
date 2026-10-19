package com.emin.e_commerce.repository;

import com.emin.e_commerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomerId(Long customerId);
}
