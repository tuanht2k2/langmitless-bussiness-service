package com.kma.engfinity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question_options")
public class QuestionOption {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID )
  private String id;

  @Column(name = "content")
  private String content;

  @Column(name = "is_correct")
  private boolean isCorrect;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private Question question;

}
