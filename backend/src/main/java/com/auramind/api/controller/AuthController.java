package com.auramind.api.controller;

import com.auramind.api.dto.AuthDTOs;
import com.auramind.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService auth;

  @PostMapping("/register")
  public AuthDTOs.AuthResponse register(@Valid @RequestBody AuthDTOs.RegisterRequest req){ return auth.register(req); }

  @PostMapping("/login")
  public AuthDTOs.AuthResponse login(@Valid @RequestBody AuthDTOs.LoginRequest req){ return auth.login(req); }
}
