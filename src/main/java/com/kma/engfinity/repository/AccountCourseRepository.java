package com.kma.engfinity.repository;

import com.kma.engfinity.entity.AccountsCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCourseRepository extends JpaRepository<AccountsCourses, String> {

    boolean existsByAccountIdAndCourseId(String id, String courseId);
}
