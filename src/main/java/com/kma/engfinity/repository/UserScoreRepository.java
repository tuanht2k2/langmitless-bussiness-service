package com.kma.engfinity.repository;

import com.kma.engfinity.DTO.response.TopicScoreResponse;
import com.kma.engfinity.entity.UserScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreRepository extends JpaRepository<UserScore, String> {
  @Query("""
    SELECT new com.kma.engfinity.DTO.response.TopicScoreResponse(
        t.id, tg.name,
        SUM(us.score),
        AVG(us.score)
    )
    FROM UserScore us
    JOIN Question q ON us.questionId = q.id
    JOIN Topic t ON q.topic.id = t.id
    JOIN Tag tg ON t.tag = tg.id
    WHERE us.userId = :userId
    GROUP BY t.id, tg.name
""")
  List<TopicScoreResponse> getUserScoreGroupedByTopic(@Param("userId") String userId);
}
