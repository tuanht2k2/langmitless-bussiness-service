package com.kma.engfinity.repository;

import com.kma.engfinity.entity.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailStatusRepository extends JpaRepository<EmailStatus, Integer> {
    Optional<EmailStatus> findByEmail(String email);
}
