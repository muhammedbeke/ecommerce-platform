package com.emin.e_commerce.data;

import java.util.List;
import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.emin.e_commerce.model.Role;
import com.emin.e_commerce.model.Customer;
import com.emin.e_commerce.model.ListOfValues;
import com.emin.e_commerce.repository.RoleRepository;
import com.emin.e_commerce.repository.CustomerRepository;
import com.emin.e_commerce.repository.ListOfValuesRepository;
import com.emin.e_commerce.util.PasswordEncoder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitiliazer implements ApplicationListener<ApplicationReadyEvent> {
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ListOfValuesRepository listOfValuesRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ADMIN", "CUSTOMER");
        List<String> statuses = List.of("PENDING", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED");
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();   
        createDefaultOrderStatus(statuses); 
    }

    private void createDefaultOrderStatus(List<String> statuses) {
       final String type = "ORDER_STATUS";
        statuses.stream()
            .filter(code -> !listOfValuesRepository.existsByTypeAndCode(type, code))
            .forEach(code -> {
                ListOfValues lov = new ListOfValues();
                lov.setType(type);
                lov.setCode(code);
                lov.setValue(code);
                listOfValuesRepository.save(lov);
            });
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
            .filter(role -> roleRepository.findByName(role).isEmpty())
            .map(Role::new)
            .forEach(roleRepository::save);
    }
    
    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ADMIN").get();
        String defaultEmail = "admin@email.com";
        String defaultPassword = "123456";

        Customer customer = customerRepository.findByEmail(defaultEmail).orElse(null);
        if (customer == null) {
            customer = new Customer();
            customer.setEmail(defaultEmail);
            customer.setPhone("0000000000");
            customer.setPassword(passwordEncoder.encode(defaultPassword));
            customer.setRole(adminRole);
            customerRepository.save(customer);
            System.out.println("Default admin user created successfully.");
        } else {
            if (customer.getPassword() == null || !passwordEncoder.matches(defaultPassword, customer.getPassword())) {
                customer.setPassword(passwordEncoder.encode(defaultPassword));
                customerRepository.save(customer);
                System.out.println("Default admin user password updated to encoded value successfully.");
            }
        }
    }
}
