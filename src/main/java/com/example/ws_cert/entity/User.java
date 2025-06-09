package com.example.ws_cert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_password")
    private String password;

    //    @Column(name = "user_certificate")
    //    private String userCertificate;
    @ManyToMany
    private Set<Role> roles;
}
