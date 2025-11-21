package com.auramind.api.model;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String displayName;
    private String password;
}
