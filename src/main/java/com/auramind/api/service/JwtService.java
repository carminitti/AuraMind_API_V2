package com.auramind.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

  private final SecretKey key;

  public JwtService(@Value("${app.jwt.secret}") String secret) {
    // Se o segredo for texto puro (não Base64):
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    // Se usar Base64, troque por:
    // this.key = Keys.hmacShaKeyFor(io.jsonwebtoken.io.Decoders.BASE64.decode(secret));
  }

  public String issue(Long userId, String email) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(email)               // API 0.12.x
        .claim("uid", userId)
        .issuedAt(Date.from(now))
        // .expiration(Date.from(now.plusMillis(expirationMs))) // (se quiser expiração, reative)
        .signWith(key)
        .compact();
  }

  public Long parseUserId(String token) {
    var claims = Jwts.parser().verifyWith(key).build() // 0.12.x
        .parseSignedClaims(token)
        .getPayload();

    Object uid = claims.get("uid");
    if (uid instanceof Integer) return ((Integer) uid).longValue();
    if (uid instanceof Long) return (Long) uid;
    if (uid instanceof String) return Long.parseLong((String) uid);
    throw new IllegalArgumentException("uid inválido no token");
  }

  public String extractSubject(String token) {
    return Jwts.parser().verifyWith(key).build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}
