package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionScoreResponse {
    private String topicId;
    private String userId;
    private String transactionId;
    private Float score;
    private List<QuestionScoreResponseV2> questions;
}
