package com.kma.engfinity.repository;

import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query(value = "SELECT c.id AS course_id, c.language AS course_language, c.name AS course_name, " +
            "c.description AS course_description, c.cost AS course_cost, c.created_at AS course_created_at, " +
            "a.id AS account_id, a.name AS account_name, a.status AS account_status, a.profile_image AS account_profile_image, " +
            "t.id AS topic_id, t.description AS topic_description, " +
            "ta.id AS tag_id, ta.name AS tag_name, t.type " +
            "FROM courses c " +
            "INNER JOIN topics t ON c.id = t.course " +
            "INNER JOIN accounts a ON c.created_by = a.id " +
            "INNER JOIN tags ta ON t.tag = ta.id " +
            "WHERE c.id = :courseId",
            nativeQuery = true)
    List<Object[]> getCourseDetails(@Param("courseId") String courseId);

    @Query(value = "SELECT c.id, c.language, c.name, c.description, c.cost, " +
            "a.id, a.name " +
            "FROM courses c " +
            "INNER JOIN accounts a " +
            "ON c.created_by = a.id " +
            "WHERE (c.name LIKE CONCAT('%', :keyword, '%') " +
            "AND (:createdBy IS NULL OR c.created_by = :createdBy)) " +
            "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Object[]> search(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("keyword") String keyword,
            @Param("createdBy") String createdBy
    );

    @Query(value = """
    SELECT * FROM courses c 
    WHERE c.language = :language
    AND (:level IS NULL OR c.level = :level)
    AND (:minCost IS NULL OR c.cost > :minCost)
    AND (:maxCost IS NULL OR c.cost <= :maxCost)
    AND (:name IS NULL OR c.name LIKE CONCAT('%', :name, '%'))
    """, nativeQuery = true)
    List<Course> aiSearchCourse(
            @Param("language") String language,
            @Param("level") Byte level,
            @Param("minCost") Integer minCost,
            @Param("maxCost") Integer maxCost,
            @Param("name") String name
    );

    @Query(value = """
        SELECT new com.kma.engfinity.DTO.response.PublicAccountResponse(a.id, a.name, a.phoneNumber, a.profileImage) FROM Course c
        JOIN AccountsCourses ac
        ON c.id = ac.courseId
        JOIN Account a
        ON a.id = ac.accountId
        WHERE c.id = :courseId
    """)
    List<PublicAccountResponse> getMembers(@Param("courseId") String courseId);
}
