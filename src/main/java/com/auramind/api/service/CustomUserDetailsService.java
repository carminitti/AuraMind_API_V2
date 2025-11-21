package com.auramind.api.service;

import com.auramind.api.model.User;
import com.auramind.api.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> opt = repo.findByEmail(usernameOrEmail);
        if (opt.isEmpty()) {
            // tenta buscar por id como fallback (se username vier como id)
            try {
                Long id = Long.parseLong(usernameOrEmail);
                User u = repo.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + usernameOrEmail));
                return mapToUserDetails(u);
            } catch (NumberFormatException ex) {
                throw new UsernameNotFoundException("Usuário não encontrado: " + usernameOrEmail);
            }
        }
        return mapToUserDetails(opt.get());
    }

    private UserDetails mapToUserDetails(User u) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return org.springframework.security.core.userdetails.User.withUsername(u.getEmail())
                .password(u.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
