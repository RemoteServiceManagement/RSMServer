package com.rsm.user.service;

import com.rsm.user.User;

import java.util.List;
import java.util.Optional;

public interface BaseUserService<T extends User> {
    List<T> findAll();
    Optional<T> findById(Long id);
    Optional<T> findByUsername(String username);
    void save(T User);
    T saveAndReturn(T User);
    void delete(T User);
    void deleteById(Long id);
}
