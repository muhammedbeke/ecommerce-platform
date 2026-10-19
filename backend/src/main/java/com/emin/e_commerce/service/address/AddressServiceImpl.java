package com.emin.e_commerce.service.address;

import com.emin.e_commerce.dto.AddressDto;
import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Address;
import com.emin.e_commerce.model.Customer;
import com.emin.e_commerce.repository.AddressRepository;
import com.emin.e_commerce.request.AddressRequest;
import com.emin.e_commerce.service.customer.ICustomerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private final AddressRepository addressRepository;
    private final ICustomerService customerService;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public AddressDto createAddress(AddressRequest request) {
        Customer customer = customerService.getCustomerEntityById(request.getCustomerId());

        Address address = new Address();
        address.setTitle(request.getTitle());
        address.setAddressLine(request.getAddressLine());
        address.setCustomer(customer);

        Address savedAddress = addressRepository.save(address);
        return dtoMapper.toAddressDto(savedAddress);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long id, AddressRequest request) {
        Address address = getAddressEntityById(id);

        if (request.getTitle() != null) {
            address.setTitle(request.getTitle());
        }
        if (request.getAddressLine() != null) {
            address.setAddressLine(request.getAddressLine());
        }
        if (request.getCustomerId() != null) {
            Customer customer = customerService.getCustomerEntityById(request.getCustomerId());
            address.setCustomer(customer);
        }

        Address updatedAddress = addressRepository.save(address);
        return dtoMapper.toAddressDto(updatedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getAddressById(Long id) {
        return dtoMapper.toAddressDto(getAddressEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressEntityById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Address not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> getAddressesByCustomerId(Long customerId) {
        customerService.getCustomerEntityById(customerId);
        List<Address> addresses = addressRepository.findByCustomerId(customerId);
        if (addresses.isEmpty()) {
            throw new NotFoundException("No addresses found for Customer ID: " + customerId);
        }
        return addresses.stream().map(dtoMapper::toAddressDto).toList();
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        Address address = getAddressEntityById(id);
        addressRepository.delete(address);
    }
}
