package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.BuyingDto;
import com.emin.e_commerce.request.BuyingRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.buying.IBuyingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/buyings")
public class BuyingController {

    private final IBuyingService buyingService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createBuying(@RequestBody BuyingRequest request) {
        BuyingDto dto = buyingService.createBuying(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Buying record created successfully!", dto));
    }

    @GetMapping("/buying/{id}")
    public ResponseEntity<ApiResponse> getBuyingById(@PathVariable Long id) {
        BuyingDto dto = buyingService.getBuyingById(id);
        return ResponseEntity.ok(new ApiResponse("Buying record retrieved successfully!", dto));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllBuyings() {
        List<BuyingDto> dtos = buyingService.getAllBuyings();
        return ResponseEntity.ok(new ApiResponse("All buying records retrieved successfully!", dtos));
    }

    @DeleteMapping("/buying/{id}/delete")
    public ResponseEntity<ApiResponse> deleteBuying(@PathVariable Long id) {
        buyingService.deleteBuying(id);
        return ResponseEntity.ok(new ApiResponse("Buying record deleted successfully!", null));
    }
}
