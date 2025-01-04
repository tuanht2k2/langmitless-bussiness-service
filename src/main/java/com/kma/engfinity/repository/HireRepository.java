package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Hire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HireRepository extends JpaRepository<Hire, String> {
}
