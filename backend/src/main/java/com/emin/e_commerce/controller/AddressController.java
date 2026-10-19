package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.AddressDto;
import com.emin.e_commerce.request.AddressRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.security.CustomUserDetails;
import com.emin.e_commerce.service.address.IAddressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final IAddressService addressService;

    @GetMapping("/my-addresses")
    public ResponseEntity<ApiResponse> getMyAddresses(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<AddressDto> dtos = addressService.getAddressesByCustomerId(userDetails.getCustomer().getId());
        return ResponseEntity.ok(new ApiResponse("Addresses retrieved successfully!", dtos));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getAddressesByCustomerId(@PathVariable Long customerId) {
        List<AddressDto> dtos = addressService.getAddressesByCustomerId(customerId);
        return ResponseEntity.ok(new ApiResponse("Addresses retrieved successfully!", dtos));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<ApiResponse> getAddressById(@PathVariable Long id) {
        AddressDto dto = addressService.getAddressById(id);
        return ResponseEntity.ok(new ApiResponse("Address retrieved successfully!", dto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createAddress(@Valid @RequestBody AddressRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (request.getCustomerId() == null && userDetails != null && userDetails.getCustomer() != null) {
            request.setCustomerId(userDetails.getCustomer().getId());
        }
        AddressDto dto = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Address created successfully!", dto));
    }

    @PutMapping("/address/{id}/update")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressRequest request) {
        AddressDto dto = addressService.updateAddress(id, request);
        return ResponseEntity.ok(new ApiResponse("Address updated successfully!", dto));
    }

    @DeleteMapping("/address/{id}/delete")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(new ApiResponse("Address deleted successfully!", null));
    }
}

