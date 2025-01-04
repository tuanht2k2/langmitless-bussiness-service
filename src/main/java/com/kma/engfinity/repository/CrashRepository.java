package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Crash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrashRepository extends JpaRepository<Crash, String> {
}
