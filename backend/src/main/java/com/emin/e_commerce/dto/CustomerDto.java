package com.emin.e_commerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerDto {
    private Long id;
    private String email;
    private String phone;
    private String role;
    private List<AddressDto> addresses;
}
