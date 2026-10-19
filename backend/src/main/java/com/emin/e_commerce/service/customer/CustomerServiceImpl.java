package com.emin.e_commerce.service.customer;

import com.emin.e_commerce.dto.CustomerDto;
import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.exception.AlreadyExistsException;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Customer;
import com.emin.e_commerce.repository.CustomerRepository;
import com.emin.e_commerce.request.CustomerRequest;
import com.emin.e_commerce.service.role.IRoleService;
import com.emin.e_commerce.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final DtoMapper dtoMapper;
    private final IRoleService roleService;

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerRequest request) {
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("Email is already registered: " + request.getEmail());
        }
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException("Phone number is already registered: " + request.getPhone());
        }

        Customer customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(roleService.getRoleEntityByName("CUSTOMER"));

        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.toCustomerDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerRequest request) {
        Customer customer = getCustomerEntityById(id);

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(customer.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new AlreadyExistsException("Email is already registered: " + request.getEmail());
            }
            customer.setEmail(request.getEmail());
        }

        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone())) {
            if (customerRepository.existsByPhone(request.getPhone())) {
                throw new AlreadyExistsException("Phone number is already registered: " + request.getPhone());
            }
            customer.setPhone(request.getPhone());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                if (request.getOldPassword() == null || request.getOldPassword().isEmpty()) {
                    throw new IllegalArgumentException("Old password is required to change password");
                }
                if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
                    throw new IllegalArgumentException("Incorrect old password");
                }
                customer.setPassword(passwordEncoder.encode(request.getPassword()));
            }
        }


        Customer updatedCustomer = customerRepository.save(customer);
        return dtoMapper.toCustomerDto(updatedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        return dtoMapper.toCustomerDto(getCustomerEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(dtoMapper::toCustomerDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerEntityById(id);
        customerRepository.delete(customer);
    }
}
