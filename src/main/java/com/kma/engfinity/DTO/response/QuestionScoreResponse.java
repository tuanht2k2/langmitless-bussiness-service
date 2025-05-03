package com.kma.engfinity.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionScoreResponse {
  private Float score;
  private Float pronunciationScore;
  private String answeredText;
}
