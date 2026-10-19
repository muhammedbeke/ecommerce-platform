package com.emin.e_commerce.request;

import lombok.Data;
import java.util.List;

@Data
public class BuyingRequest {
    private List<BuyingItemRequest> items;
}
