package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicScoreResponse {
  private String topic;
  private String tag;
  private Double totalScore;
  private Double averageScore;
}
