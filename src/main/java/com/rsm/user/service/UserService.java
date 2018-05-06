package com.rsm.user.service;

import com.rsm.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void save(User User);
    User saveAndReturn(User User);
    void delete(User User);
    void deleteById(Long id);
}
