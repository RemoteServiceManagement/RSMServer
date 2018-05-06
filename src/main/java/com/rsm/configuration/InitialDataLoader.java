package com.rsm.configuration;

import com.rsm.user.User;
import com.rsm.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        //temporary: execute script data.sql and login on adam_adamowicz instead of admin
 /*       if (!userRepository.findByUsername("admin").isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEnabled(true);
            userRepository.save(user);
        }*/
    }
}
