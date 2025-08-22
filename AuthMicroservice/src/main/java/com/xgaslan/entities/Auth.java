package com.xgaslan.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tbl_auth")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String password;

    private String email;

    Boolean isActive;

    LocalDateTime createdAt;
}
