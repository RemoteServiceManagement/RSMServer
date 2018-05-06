package com.rsm.customer;


import com.rsm.user.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    Optional<Customer> findByUser(User user);
    void save(Customer customer);
    Customer saveAndReturn(Customer customer);
    void delete(Customer customer);
    void deleteById(Long id);
}
