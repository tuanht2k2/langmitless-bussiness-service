package com.kma.engfinity.repository;

import com.kma.engfinity.entity.ClassMember;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMember, String> {

  boolean existsByCourseIdAndAccountId(String courseId, String accountId);

  List<ClassMember> findByCourseId(String courseId, Pageable pageable);

}
