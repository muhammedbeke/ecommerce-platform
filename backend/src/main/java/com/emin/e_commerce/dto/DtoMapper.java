package com.emin.e_commerce.dto;

import com.emin.e_commerce.model.*;
import com.emin.e_commerce.repository.ProductPricingRepository;
import com.emin.e_commerce.repository.StockRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoMapper {

    private final ProductPricingRepository pricingRepository;
    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    public CategoryDto toCategoryDto(Category category) {
        if (category == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        if (category.getParentCategory() != null) {
            dto.setParentCategoryId(category.getParentCategory().getId());
        }
        return dto;
    }

    public ProductDto toProductDtoWithoutCategory(Product product) {
        if (product == null) return null;
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        
        BigDecimal currentPrice = pricingRepository.findFirstByProductIdOrderByCreatedDatetimeDesc(product.getId())
                .map(ProductPricing::getPrice)
                .orElse(BigDecimal.ZERO);
        dto.setPrice(currentPrice);
        
        int stockQuantity = stockRepository.findByProductId(product.getId())
                .map(Stock::getQuantity)
                .orElse(0);
        dto.setStock(stockQuantity);
        
        dto.setCategory(null);
        
        if (product.getImages() != null) {
            dto.setImages(product.getImages().stream().map(this::toImageDto).collect(Collectors.toList()));
        }
        if (dto.getLowStockThreshold() == null) {
            dto.setLowStockThreshold(10);
        }
        
        return dto;
    }

    public ImageDto toImageDto(Image image) {
        if (image == null) return null;
      return modelMapper.map(image, ImageDto.class);
    }

    public ProductDto toProductDto(Product product) {
        if (product == null) return null;
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        
        BigDecimal currentPrice = pricingRepository.findFirstByProductIdOrderByCreatedDatetimeDesc(product.getId())
                .map(ProductPricing::getPrice)
                .orElse(BigDecimal.ZERO);
        dto.setPrice(currentPrice);
        
        int stockQuantity = stockRepository.findByProductId(product.getId())
                .map(Stock::getQuantity)
                .orElse(0);
        dto.setStock(stockQuantity);
        
        dto.setCategory(toCategoryDto(product.getCategory()));
        
        if (product.getImages() != null) {
            dto.setImages(product.getImages().stream().map(this::toImageDto).collect(Collectors.toList()));
        }
        if (dto.getLowStockThreshold() == null) {
            dto.setLowStockThreshold(10);
        }
        
        return dto;
    }

    public AddressDto toAddressDto(Address address) {
        if (address == null) return null;
        AddressDto dto = modelMapper.map(address, AddressDto.class);
        if (address.getCustomer() != null) {
            dto.setCustomerId(address.getCustomer().getId());
        }
        return dto;
    }
    public ListOfValuesDto toListOfValuesDto(ListOfValues listOfValues) {
        if (listOfValues == null) return null;
        ListOfValuesDto dto = modelMapper.map(listOfValues, ListOfValuesDto.class);
        dto.setId(listOfValues.getId());
        return dto;
    }

    public CustomerDto toCustomerDto(Customer customer) {
        if (customer == null) return null;
        CustomerDto dto = modelMapper.map(customer, CustomerDto.class);
        dto.setRole(customer.getRole() != null ? customer.getRole().getName() : null);
        if (customer.getAddresses() != null) {
            dto.setAddresses(customer.getAddresses().stream().map(this::toAddressDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public RoleDto toRoleDto(Role role) {
        if (role == null) return null;
        RoleDto dto = modelMapper.map(role, RoleDto.class);
        return dto;
    }

    public OrderDetailDto toOrderDetailDto(OrderDetail detail) {
        if (detail == null) return null;
        OrderDetailDto dto = modelMapper.map(detail, OrderDetailDto.class);
        dto.setId(detail.getId());
        if (detail.getProduct() != null) {
            dto.setProductId(detail.getProduct().getId());
            dto.setProductName(detail.getProduct().getName());
        }
        return dto;
    }

    public OrderDto toOrderDto(Order order) {
        if (order == null) return null;
        OrderDto dto = modelMapper.map(order, OrderDto.class);
        dto.setId(order.getId());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
        }
        dto.setOrderStatus(order.getOrderStatus() != null ? order.getOrderStatus().getValue() : null);
        if (order.getOrderDetails() != null) {
            dto.setOrderDetails(order.getOrderDetails().stream().map(this::toOrderDetailDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public BuyingDetailDto toBuyingDetailDto(BuyingDetail detail) {
        if (detail == null) return null;
        BuyingDetailDto dto = modelMapper.map(detail, BuyingDetailDto.class);
        if (detail.getProduct() != null) {
            dto.setProductId(detail.getProduct().getId());
            dto.setProductName(detail.getProduct().getName());
        }
        return dto;
    }

    public BuyingDto toBuyingDto(Buying buying) {
        if (buying == null) return null;
        BuyingDto dto = modelMapper.map(buying,BuyingDto.class);
        if (buying.getBuyingDetails() != null) {
            dto.setBuyingDetails(buying.getBuyingDetails().stream().map(this::toBuyingDetailDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public StockDto toStockDto(Stock stock) {
        if (stock == null) return null;
        StockDto dto = modelMapper.map(stock, StockDto.class);
        if (stock.getProduct() != null) {
            dto.setProductId(stock.getProduct().getId());
        }
        return dto;
    }

    public CartItemDto toCartItemDto(CartItem item) {
        if (item == null) return null;
        CartItemDto dto = modelMapper.map(item, CartItemDto.class);
        if (item.getProduct() != null) {
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
        }
        return dto;
    }

    public CartDto toCartDto(Cart cart) {
        if (cart == null) return null;
        CartDto dto = modelMapper.map(cart, CartDto.class);
        if (cart.getCartItems() != null) {
            dto.setCartItems(cart.getCartItems().stream().map(this::toCartItemDto).collect(Collectors.toList()));
        }
        return dto;
    }
}
