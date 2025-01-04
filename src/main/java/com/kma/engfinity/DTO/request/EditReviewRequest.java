package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EReviewType;
import lombok.Data;

@Data
public class EditReviewRequest {
    private String id;
    private EReviewType type;
    private String content;
    private Integer rating;
    private String reviewedAccountId;
    private String reviewedTopicId;
}
