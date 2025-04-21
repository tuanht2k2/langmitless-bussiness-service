package com.kma.engfinity.repository;

import com.kma.engfinity.entity.Question;
import com.kma.engfinity.enums.QuestionType;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

  @Query("select q from Question q where q.topic.id = :topicId and "
      + "(:questionType is null or q.questionType = :questionType)")
  Page<Question> searchQuestion(@Param("topicId") String topicId, @Param("questionType")
  QuestionType questionType, Pageable pageable);

  @NotNull
  List<Question> findAllById(@NotNull Iterable<String> ids);

  List<Question> findByTopicId(String topicId);

}
