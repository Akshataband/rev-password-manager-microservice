package com.rev.vault_service.repository;

import com.rev.vault_service.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findByUserEmail(String email);

    List<Password> findByUserEmailAndFavoriteTrue(String email);

    List<Password> findByUserEmailAndSiteNameContainingIgnoreCase(String email,String keyword);

    List<Password> findByUserEmailAndCategory(String email,String category);

    Optional<Password> findTopByUserEmailOrderByCreatedAtDesc(String email);
}