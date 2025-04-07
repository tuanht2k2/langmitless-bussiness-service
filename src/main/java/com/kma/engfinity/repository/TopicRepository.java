package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String> {
    @Query(value = "SELECT tp.id, tp.description, " +
            "ta.id, ta.name " +
            "FROM topics tp " +
            "INNER JOIN tags ta " +
            "ON tp.tag = ta.id " +
            "WHERE (:courseId = tp.course)", nativeQuery = true)
    List<Object[]> getAllByCourseId(
            @Param("courseId") String courseId
    );

    List<Topic> findByCourse(String courseId);
}
