package com.kma.engfinity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_scores")
public class UserScore {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID )
  private String id;

  @Column(name = "score", nullable = false)
  private Float score;

  @Column(name = "answered_text", columnDefinition = "TEXT")
  private String answeredText;

  @Column(name = "pronunciation_score")
  private Float pronunciationScore;

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private Instant createdAt;

  @Column(name = "topic_Id" , nullable = false)
  private String topicId;

  @Column(name = "question_id",nullable = false)
  private String questionId;

  @Column(name = "user_id" , nullable = false)
  private String userId;

  @Column(name = "transaction_id", nullable = false)
  private String transactionId;


  private String audioUrl;
}
