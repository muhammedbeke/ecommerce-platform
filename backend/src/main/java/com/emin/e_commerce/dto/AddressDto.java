package com.emin.e_commerce.dto;

import lombok.Data;

@Data
public class AddressDto {
    private Long id;
    private String title;
    private String addressLine;
    private Long customerId;
}
