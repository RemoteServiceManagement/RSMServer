package com.rsm.customer;

import com.rsm.device.Device;
import com.rsm.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Optional<Authentication> authentication;

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Customer saveAndReturn(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Device> findCustomersDevices(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customer.getDevices()).orElseThrow((() -> new CustomerDoesNotExistException()));
    }

    @Override
    public Optional<Customer> getCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return customerRepository.findByUsername(authentication.getName());
    }
}
