package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Crash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CrashRepository extends JpaRepository<Crash, String> {
    @Query(value = "SELECT * FROM crashes WHERE created_at > :from AND created_at < :to", nativeQuery = true)
    List<Crash> statistics(@Param("from") Date from, @Param("to") Date to);
}
