package com.kma.engfinity.DTO.request;

import com.google.firebase.database.annotations.NotNull;
import com.kma.engfinity.enums.QuestionType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseQuestionRequest {
  @NotNull
  private UUID topicId;

  private QuestionType questionType;

  private Integer limit;

}
