package com.rsm.user.service;

import com.rsm.common.ApplicationService;
import com.rsm.user.USER_ROLE;
import com.rsm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.EnumSet;

@RequiredArgsConstructor
@ApplicationService
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(user -> User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(EnumSet.allOf(USER_ROLE.class).stream().map(Enum::name).toArray(String[]::new))
                .disabled(false)
                .build()
        ).orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
    }
}
