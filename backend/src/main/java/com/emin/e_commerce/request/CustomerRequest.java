package com.emin.e_commerce.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String phone;
    @Size(min = 6, max = 20, message = "Password must be between 6-20 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
        message = "Password must contain upper, lower case letters and a number"
    )
    private String password;
    private String oldPassword;
}
