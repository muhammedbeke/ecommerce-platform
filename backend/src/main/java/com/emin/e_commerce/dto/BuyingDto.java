package com.emin.e_commerce.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BuyingDto {
    private Long id;
    private LocalDateTime purchaseDate;
    private List<BuyingDetailDto> buyingDetails;
}
