package com.rsm.user.service;

import com.rsm.customer.Customer;
import com.rsm.employee.Employee;
import com.rsm.role.Role;
import com.rsm.role.RoleDoesNotExistException;
import com.rsm.role.RoleRepository;
import com.rsm.user.NewUserDto;
import com.rsm.user.RoleEnum;
import com.rsm.user.User;
import com.rsm.user.UserDetails;
import com.rsm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void delete(User User) { userRepository.delete(User); }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }

    @Override
    public User createByNewUserDto(NewUserDto newUser) {
        Role role = roleRepository.findById(newUser.getRoleId()).orElseThrow(RoleDoesNotExistException::new);
        User user = getEntityByRole(role);
        user.setDetails(new UserDetails());
        user.setUsername(newUser.getLogin());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setCredentialsExpired(false);
        user.setEnabled(true);
        user.setExpired(false);
        user.setLocked(false);
        user.setRoles(Collections.singleton(role));
        user.getDetails().setEmail(newUser.getEmail());
        user.getDetails().setFirstName(newUser.getFirstName());
        user.getDetails().setLastName(newUser.getLastName());
        user.getDetails().setPhone(newUser.getPhone());
        userRepository.save(user);
        return userRepository.findById(user.getId()).get();
    }

    private User getEntityByRole(Role role) {
        switch (RoleEnum.valueOf(role.getRoleName())) {
            case ADMIN:
            case MASTER:
            case EMPLOYEE:
                return new Employee();
            case CUSTOMER:
                return new Customer();
        }
        throw new RoleDoesNotExistException(role.getRoleName() + " does not exist");
    }
}
