package com.emin.e_commerce.service.buying;

import com.emin.e_commerce.dto.BuyingDto;
import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Buying;
import com.emin.e_commerce.model.BuyingDetail;
import com.emin.e_commerce.model.Product;
import com.emin.e_commerce.repository.BuyingRepository;
import com.emin.e_commerce.request.BuyingItemRequest;
import com.emin.e_commerce.request.BuyingRequest;
import com.emin.e_commerce.service.product.IProductService;
import com.emin.e_commerce.service.stock.IStockService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyingServiceImpl implements IBuyingService {

    private final BuyingRepository buyingRepository;
    private final IProductService productService;
    private final IStockService stockService;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public BuyingDto createBuying(BuyingRequest request) {
        Buying buying = new Buying();
        buying.setPurchaseDate(LocalDateTime.now());

        if (request.getItems() != null) {
            for (BuyingItemRequest item : request.getItems()) {
                Product product = productService.getProductEntityById(item.getProductId());

                stockService.updateStock(item.getProductId(), item.getQuantity());

                BuyingDetail buyingDetail = new BuyingDetail();
                buyingDetail.setBuying(buying);
                buyingDetail.setProduct(product);
                buyingDetail.setQuantity(item.getQuantity());
                buyingDetail.setPrice(item.getPrice());

                buying.getBuyingDetails().add(buyingDetail);
            }
        }

        Buying savedBuying = buyingRepository.save(buying);
        return dtoMapper.toBuyingDto(savedBuying);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyingDto getBuyingById(Long id) {
        return dtoMapper.toBuyingDto(getBuyingEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Buying getBuyingEntityById(Long id) {
        return buyingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buying record not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyingDto> getAllBuyings() {
        return buyingRepository.findAll().stream()
                .map(dtoMapper::toBuyingDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBuying(Long id) {
        Buying buying = getBuyingEntityById(id);
        buyingRepository.delete(buying);
    }
}
