package com.auramind.api.controller;

import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
  private final UserRepository users;

  @GetMapping("/me")
  public MeResponse me(Authentication auth){
    Long uid = (Long) auth.getPrincipal();
    User u = users.findById(uid).orElseThrow();
    var resp = new MeResponse();
    resp.setId(u.getId());
    resp.setEmail(u.getEmail());
    resp.setDisplayName(u.getDisplayName());
    return resp;
  }

  @Data static class MeResponse {
    private Long id; private String email; private String displayName;
  }
}
