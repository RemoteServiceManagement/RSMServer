package com.rsm.user.service;

import com.rsm.user.User;
import com.rsm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User>findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User User) {
        userRepository.save(User);
    }

    @Override
    public User saveAndReturn(User User) {
        return userRepository.save(User);
    }

    @Override
    public void delete(User User) {
        userRepository.delete(User);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
}
