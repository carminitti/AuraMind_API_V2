package com.auramind.api.service;

import com.auramind.api.dto.AuthDtos.*;
import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import com.auramind.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwt;

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email()))
            throw new RuntimeException("Email already registered");

        User u = new User();
        u.setEmail(req.email());
        u.setDisplayName(req.displayName());
        u.setPasswordHash(encoder.encode(req.password())); // <-- FIX

        userRepo.save(u);

        String token = jwt.generateToken(u.getId().toString());

        return new AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(req.password(), u.getPasswordHash())) // <-- FIX
            throw new RuntimeException("Invalid credentials");

        String token = jwt.generateToken(u.getId().toString());

        return new AuthResponse(u.getId(), u.getEmail(), u.getDisplayName(), token);
    }
}
