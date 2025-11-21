package com.auramind.api.service;
import org.springframework.stereotype.Service;
import java.util.Collections;

import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;

   @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPasswordHash(), Collections.emptyList());
}

               
    }

