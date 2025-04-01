package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Hire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HireRepository extends JpaRepository<Hire, String> {
    @Query("SELECT h from Hire h WHERE h.teacher.id = :teacherId AND h.createdBy.id = :createdBy ORDER BY h.createdAt DESC")
    List<Hire> findByCreatedByAndTeacher(@Param("createdBy") String createdBy, @Param("teacherId") String teacherId);
}
