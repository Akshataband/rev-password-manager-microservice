package com.rev.vault_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passwords")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Instead of userId we store userEmail from JWT
    @Column(nullable = false)
    private String userEmail;

    private String siteName;

    private String username;

    private String encryptedPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}