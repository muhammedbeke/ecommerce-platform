package com.emin.e_commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ListOfValuesRequest {

    @NotBlank(message = "Code can't be blank")
    private String code;

    @NotBlank(message = "Value can't be blank")
    private String value;

    @NotBlank(message = "Type can't be blank")
    private String type;
}

