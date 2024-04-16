package com.profile.in.repository;

import com.profile.in.model.OtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OtpConfigRepository extends JpaRepository<OtpConfig, Long> {
    Optional<OtpConfig> findAllByUsername(String username);
}
