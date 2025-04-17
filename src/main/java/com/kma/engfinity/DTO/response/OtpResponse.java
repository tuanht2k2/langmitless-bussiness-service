package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {
    private boolean isCorrect;
    private Integer remainSent;
    private Integer retryTime;
}
