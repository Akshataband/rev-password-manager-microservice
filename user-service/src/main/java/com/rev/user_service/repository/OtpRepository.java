package com.rev.user_service.repository;

import com.rev.user_service.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findTopByEmailOrderByExpiryTimeDesc(String email);
    void deleteByEmail(String email);

}