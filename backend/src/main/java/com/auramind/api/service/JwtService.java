package com.auramind.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
  private final byte[] key;

  public JwtService(@Value("${app.jwt.secret}") String secret) {
    this.key = secret.getBytes();
  }

  public String issue(Long userId, String email){
    Instant now = Instant.now();
    return Jwts.builder()
      .subject(email)
      .claim("uid", userId)
      .issuedAt(Date.from(now))
      .expiration(Date.from(now.plusSeconds(60*60*24*7)))
      .signWith(Keys.hmacShaKeyFor(key))
      .compact();
  }

  public Long parseUserId(String token){
    var body = Jwts.parserBuilder()
      .setSigningKey(Keys.hmacShaKeyFor(key)).build()
      .parseClaimsJws(token).getBody();
    Object uid = body.get("uid");
    if (uid instanceof Integer) return ((Integer)uid).longValue();
    return (Long) uid;
  }
}
