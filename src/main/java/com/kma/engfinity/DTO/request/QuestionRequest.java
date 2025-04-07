package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.QuestionType;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

  @NotNull
  private UUID courseId;

  @NotNull
  private QuestionType type;

  @NotNull
  private String content;

  private MultipartFile audioSample;

  private List<QuestionOptionRequest> options;

}
