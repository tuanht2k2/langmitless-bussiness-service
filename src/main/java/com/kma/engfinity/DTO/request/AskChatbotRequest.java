package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class AskChatbotRequest {
    private String message;
    private String courseId;
}
