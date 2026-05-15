package com.greenhouse.auth.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "owners")
@Data
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
}