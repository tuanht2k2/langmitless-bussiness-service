package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class TransactionScoreRequest {
    private String topicId;
    private String userId;
    private String transactionId;
}
