package com.kma.engfinity.repository;

import com.kma.engfinity.DTO.response.TopicScoreResponse;
import com.kma.engfinity.entity.UserScore;
import java.util.List;
import java.util.Optional;
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
    WHERE us.topicId = :topicId
    GROUP BY t.id, tg.name
  """)
  List<TopicScoreResponse> getUserScoreGroupedByTopic(@Param("topicId") String topicId);

  Optional<UserScore> findTopByTopicIdAndQuestionIdOrderByCreatedAtDesc(String topicId, String questionId);

  @Query(value = """
    SELECT
        q.id AS question_id,
        q.topic_id,
        q.question_type,
        q.content AS question_content,
        q.audio_sample,
        q.text_sample,
        us.id AS user_score_id,
        us.user_id,
        us.question_id AS answered_question_id,
        us.transaction_id,
        us.audio_url,
        us.answered_text,
        us.score,
        us.created_at,
        qo.id AS option_id,
        qo.content AS option_content,
        qo.is_correct,
        get_average_score(:topicId, :userId, :transactionId) as avg
    FROM
        questions q
    LEFT JOIN (
        SELECT
            us1.*
        FROM
            user_scores us1
        INNER JOIN (
            SELECT
                question_id,
                transaction_id,
                user_id,
                MAX(created_at) AS max_created_at
            FROM
                user_scores
            WHERE
                transaction_id = :transactionId
                AND user_id = :userId
            GROUP BY
                question_id, transaction_id, user_id
        ) latest
        ON
            us1.question_id = latest.question_id
            AND us1.transaction_id = latest.transaction_id
            AND us1.user_id = latest.user_id
            AND us1.created_at = latest.max_created_at
    ) us
    ON
        q.id = us.question_id
        AND us.transaction_id = :transactionId
        AND us.user_id = :userId
    LEFT JOIN
        question_options qo ON qo.question_id = q.id
    WHERE
        q.topic_id = :topicId
  """, nativeQuery = true)
  List<Object[]> getTopicScoreHistory(@Param("topicId") String topicId, @Param("userId") String userId, @Param("transactionId") String transactionId);

  @Query("""
    SELECT DISTINCT us.transactionId FROM com.kma.engfinity.entity.UserScore us
    WHERE us.topicId = :topicId
    AND us.userId = :userId
  """)
  List<String> searchTransactionsByTopic(@Param("topicId") String topicId, @Param("userId") String userId);
}
