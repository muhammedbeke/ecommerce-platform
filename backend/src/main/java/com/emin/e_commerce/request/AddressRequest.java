package com.emin.e_commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    private String title; 
    @NotBlank(message = "address cannot be blank")
    private String addressLine;
    private Long customerId;
}
