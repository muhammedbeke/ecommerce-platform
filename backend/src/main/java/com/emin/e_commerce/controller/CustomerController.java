package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.CustomerDto;
import com.emin.e_commerce.request.CustomerRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.customer.ICustomerService;

import com.emin.e_commerce.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCustomers() {
        List<CustomerDto> dtos = customerService.getAllCustomers();
        return ResponseEntity.ok(new ApiResponse("All customers retrieved successfully!", dtos));
    }

    @GetMapping("/customer/me")
    public ResponseEntity<ApiResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        CustomerDto dto = customerService.getCustomerById(userDetails.getCustomer().getId());
        return ResponseEntity.ok(new ApiResponse("Profile retrieved successfully!", dto));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ApiResponse> getCustomerById(@PathVariable Long id) {
        CustomerDto dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(new ApiResponse("Customer retrieved successfully!", dto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerDto dto = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Customer registered successfully!", dto));
    }

    @PutMapping("/customer/{id}/update")
    public ResponseEntity<ApiResponse> updateCustomer(@Valid @PathVariable Long id, @RequestBody CustomerRequest request) {
        CustomerDto dto = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(new ApiResponse("Customer profile updated successfully!", dto));
    }

    @DeleteMapping("/customer/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse("Customer deleted successfully!", null));
    }
}
