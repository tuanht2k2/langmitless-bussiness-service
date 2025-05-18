package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class SearchTransactionRequest {
    private String userId;
    private String topicId;
}
