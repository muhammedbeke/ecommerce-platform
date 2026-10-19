package com.emin.e_commerce.service.address;

import com.emin.e_commerce.dto.AddressDto;
import com.emin.e_commerce.model.Address;
import com.emin.e_commerce.request.AddressRequest;

import java.util.List;

public interface IAddressService {
    AddressDto createAddress(AddressRequest request);
    AddressDto updateAddress(Long id, AddressRequest request);
    AddressDto getAddressById(Long id);
    Address getAddressEntityById(Long id);
    List<AddressDto> getAddressesByCustomerId(Long customerId);
    void deleteAddress(Long id);
}
