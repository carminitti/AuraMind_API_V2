package com.auramind.api.service;

import com.auramind.api.dto.AuthDTOs;
import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

 
public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.passwordEncoder = passwordEncoder;
}

    public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest req) {
        User u = new User();
        u.setEmail(req.email());
        u.setDisplayName(req.displayName());
        u.setPassword(passwordEncoder.encode(req.password()));
        userRepository.save(u);
        var userDetails = new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), List.of());
        String token = jwtService.generateToken(userDetails);
        return new AuthDTOs.AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }

    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest req) {
        User u = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(req.password(), u.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        var userDetails = new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), List.of());
        String token = jwtService.generateToken(userDetails);
        return new AuthDTOs.AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }
}