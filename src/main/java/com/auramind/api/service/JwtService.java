package com.auramind.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private SecretKey secretKey;

    @jakarta.annotation.PostConstruct
    public void init() {
        String secret = System.getenv("JWT_SECRET");

        if (secret == null || secret.length() < 32) {
            throw new RuntimeException("ERRO: A variável JWT_SECRET não foi definida ou é muito curta!");
        }

        // Converte texto em chave
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** ----------------------
     * GERA JWT
     -----------------------*/
    public String generateToken(UserDetails user) {
        long expirationMs = 1000 * 60 * 60 * 24 * 7; // 7 dias

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    /** ----------------------
     * EXTRAI EMAIL DO TOKEN
     -----------------------*/
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /** ----------------------
     * VALIDA TOKEN
     -----------------------*/
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** ----------------------
     * PARSE CLAIMS
     -----------------------*/
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
