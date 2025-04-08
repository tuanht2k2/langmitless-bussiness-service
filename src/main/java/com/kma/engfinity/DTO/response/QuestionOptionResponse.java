package com.kma.engfinity.DTO.response;

import java.util.UUID;
import lombok.Data;

@Data
public class QuestionOptionResponse {
  private UUID id;
  private String content;
  private boolean isCorrect;
}
