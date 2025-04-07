package com.kma.engfinity.DTO.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassMemberListRequest {

  @NotNull
  private String courseId;
  private Integer page;
  private Integer size;
}
