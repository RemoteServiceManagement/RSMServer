package com.rsm.employee;


import com.rsm.user.User;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Optional<Employee> findByUser(User user);
    void save(Employee employee);
    Employee saveAndReturn(Employee employee);
    void delete(Employee employee);
    void deleteById(Long id);
}
