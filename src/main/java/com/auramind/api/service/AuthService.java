package com.auramind.api.service;

import com.auramind.api.dto.AuthDTOs;
import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
  private final UserRepository users;
  private final JwtService jwt;
  private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

  public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest req){
    users.findByEmail(req.getEmail()).ifPresent(u -> { throw new RuntimeException("Email já cadastrado"); });
    User u = new User();
    u.setEmail(req.getEmail());
    u.setDisplayName(req.getDisplayName());
    u.setPasswordHash(bcrypt.encode(req.getPassword()));
    users.save(u);
    return toAuthResponse(u);
  }

  public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest req){
    User u = users.findByEmail(req.getEmail())
        .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));
    if(!bcrypt.matches(req.getPassword(), u.getPasswordHash())){
      throw new RuntimeException("Credenciais inválidas");
    }
    return toAuthResponse(u);
  }

  private AuthDTOs.AuthResponse toAuthResponse(User u){
    AuthDTOs.AuthResponse resp = new AuthDTOs.AuthResponse();
    resp.setToken(jwt.issue(u.getId(), u.getEmail()));
    resp.setUserId(u.getId());
    resp.setDisplayName(u.getDisplayName());
    resp.setEmail(u.getEmail());
    return resp;
  }
}
