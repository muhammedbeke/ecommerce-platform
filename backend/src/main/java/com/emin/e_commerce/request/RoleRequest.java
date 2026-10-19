package com.emin.e_commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequest {
    @NotBlank(message = "Role name cannot be blank")
    private String name;
}
