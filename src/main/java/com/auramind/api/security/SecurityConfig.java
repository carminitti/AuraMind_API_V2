package com.auramind.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .cors(cors -> {}) // ✅ importante ativar o CorsFilter global abaixo
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsFilter corsFilter() {
    var source = new UrlBasedCorsConfigurationSource();
    var config = new CorsConfiguration();

    // ✅ Permite comunicação com seu app Android e browsers
    config.setAllowCredentials(true);
    config.setAllowedOriginPatterns(List.of("*")); // pode restringir se quiser
    config.setAllowedHeaders(List.of("*"));
    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
    config.setExposedHeaders(List.of("Authorization")); // importante p/ JWT

    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }
}
