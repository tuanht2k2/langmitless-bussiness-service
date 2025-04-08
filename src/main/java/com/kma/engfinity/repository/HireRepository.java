package com.kma.engfinity.repository;

import com.kma.engfinity.DTO.response.HireResponse;
import com.kma.engfinity.entity.Hire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HireRepository extends JpaRepository<Hire, String> {
    @Query("SELECT h from Hire h WHERE h.teacher.id = :teacherId ORDER BY h.createdAt DESC")
    List<Hire> findByTeacher(@Param("teacherId") String teacherId);

    @Query("SELECT new com.kma.engfinity.DTO.response.HireResponse(h.id, h.cost, h.totalTime, h.status, h.createdAt) from Hire h WHERE h.id = :id")
    HireResponse getDetail(@Param("id") String id);
}
