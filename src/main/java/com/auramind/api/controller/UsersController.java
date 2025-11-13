package com.auramind.api.controller;

import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

  // DTO de resposta para /me
  public static class MeResponse {
    private Long id;
    private String email;
    private String displayName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
  }

  private final UserRepository userRepository;

  public UsersController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public MeResponse me(@AuthenticationPrincipal UserDetails details) {
    // email do usuário logado vem do UserDetails
    String email = details.getUsername();
    User u = userRepository.findByEmail(email);

    MeResponse resp = new MeResponse();
    resp.setId(u.getId());
    resp.setEmail(u.getEmail());
    resp.setDisplayName(u.getDisplayName());

    return resp;
  }
}
