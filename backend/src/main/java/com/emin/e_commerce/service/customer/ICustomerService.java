package com.emin.e_commerce.service.customer;

import com.emin.e_commerce.dto.CustomerDto;
import com.emin.e_commerce.model.Customer;
import com.emin.e_commerce.request.CustomerRequest;

import java.util.List;

public interface ICustomerService {
    CustomerDto createCustomer(CustomerRequest request);
    CustomerDto updateCustomer(Long id, CustomerRequest request);
    CustomerDto getCustomerById(Long id);
    Customer getCustomerEntityById(Long id);
    List<CustomerDto> getAllCustomers();
    void deleteCustomer(Long id);
}
