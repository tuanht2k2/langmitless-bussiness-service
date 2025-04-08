package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class QuestionOptionRequest {
  private String content;
  private boolean isCorrect;
}
