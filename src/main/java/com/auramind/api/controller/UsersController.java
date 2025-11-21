package com.auramind.api.controller;

import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/users")
public class UsersController {

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
    public MeResponse me(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        User u = null;

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails ud) {
            String email = ud.getUsername();
            u = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + email));
        } else if (principal instanceof String pStr) {
            // assume email string
            u = userRepository.findByEmail(pStr)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado: " + pStr));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Principal inválido");
        }

        MeResponse resp = new MeResponse();
        resp.setId(u.getId());
        resp.setEmail(u.getEmail());
        resp.setDisplayName(u.getDisplayName());
        return resp;
    }
}
