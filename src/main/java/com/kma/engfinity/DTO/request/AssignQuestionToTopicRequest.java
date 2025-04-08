package com.kma.engfinity.DTO.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignQuestionToTopicRequest {

  private String topicId;
  private List<String> questionIds;
}
