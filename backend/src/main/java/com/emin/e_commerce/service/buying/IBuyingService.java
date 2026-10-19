package com.emin.e_commerce.service.buying;

import com.emin.e_commerce.dto.BuyingDto;
import com.emin.e_commerce.model.Buying;
import com.emin.e_commerce.request.BuyingRequest;

import java.util.List;

public interface IBuyingService {
    BuyingDto createBuying(BuyingRequest request);
    BuyingDto getBuyingById(Long id);
    Buying getBuyingEntityById(Long id);
    List<BuyingDto> getAllBuyings();
    void deleteBuying(Long id);
}
