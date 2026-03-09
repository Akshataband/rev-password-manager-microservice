package com.rev.password_service.entity;

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

    private Long userId;

    private String siteName;

    private String username;

    private String encryptedPassword;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}