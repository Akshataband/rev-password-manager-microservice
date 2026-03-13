package com.rev.vault_service.repository;

import com.rev.vault_service.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    List<Password> findByUserEmail(String userEmail);
}