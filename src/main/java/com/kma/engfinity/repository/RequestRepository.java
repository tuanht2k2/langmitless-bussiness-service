package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Crash;
import com.kma.engfinity.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
    @Query(value = "SELECT * FROM requests WHERE created_at > :from AND created_at < :to", nativeQuery = true)
    List<Request> statistics(@Param("from") Date from, @Param("to") Date to);
}
