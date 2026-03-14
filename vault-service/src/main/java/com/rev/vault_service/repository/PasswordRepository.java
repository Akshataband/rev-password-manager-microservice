package com.rev.vault_service.repository;

import com.rev.vault_service.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findByUserEmail(String userEmail);

    List<Password> findByUserEmailAndFavoriteTrue(String userEmail);

    List<Password> findByUserEmailAndSiteNameContainingIgnoreCase(String userEmail, String keyword);

    List<Password> findByUserEmailAndCategory(String userEmail, String category);

    Optional<Password> findTopByUserEmailOrderByCreatedAtDesc(String userEmail);

    boolean existsByUserEmailAndEncryptedPassword(String userEmail, String encryptedPassword);
}