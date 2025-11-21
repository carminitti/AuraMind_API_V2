package com.auramind.api.service;

import com.auramind.api.dto.AuthDTOs;
import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest req) {
        User u = new User();
        u.setEmail(req.getEmail());
        u.setDisplayName(req.getDisplayName());
        u.setPasswordHash(passwordEncoder.encode(req.getPassword())); // usar passwordHash
        userRepository.save(u);

        var userDetails = new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPasswordHash(), Collections.emptyList());
        String token = jwtService.generateToken(userDetails);
        return new AuthDTOs.AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }

    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest req) {
        User u = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        var userDetails = new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPasswordHash(), Collections.emptyList());
        String token = jwtService.generateToken(userDetails);
        return new AuthDTOs.AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }
}
