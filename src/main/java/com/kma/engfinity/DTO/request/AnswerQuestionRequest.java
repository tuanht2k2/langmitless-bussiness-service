package com.kma.engfinity.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerQuestionRequest {
  private String topicId;
  private String questionId;
  private String answeredText;
  private String audioUrl;
  private MultipartFile answerFile;
  private String transactionId;
}
