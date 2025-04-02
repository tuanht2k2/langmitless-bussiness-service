package com.kma.engfinity.DTO.response;

import java.time.Instant;
import lombok.Data;

@Data
public class ClassMemberResponse {
  private String accountId;
  private Instant joinedAt;
}
