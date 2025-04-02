package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.QuestionType;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class QuestionResponse {

  private UUID id;

  private UUID courseId;

  private QuestionType type;

  private String content;

  private String audioSample;

  private List<QuestionOptionResponse> option;

}
