package com.auramind.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String displayName;
  private String passwordHash;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getDisplayName() { return displayName; }
  public void setDisplayName(String displayName) { this.displayName = displayName; }

  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
