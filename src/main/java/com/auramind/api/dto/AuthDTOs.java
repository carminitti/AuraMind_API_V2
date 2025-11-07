package com.auramind.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDTOs {
  @Data public static class RegisterRequest {
    @Email @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String displayName;
  }

  @Data public static class LoginRequest {
    @Email @NotBlank private String email;
    @NotBlank private String password;
  }

  @Data public static class AuthResponse {
    private String token;
    private Long userId;
    private String displayName;
    private String email;
  }
}
