package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EReviewType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchReviewRequest extends CommonSearchRequest{
    private EReviewType type;
    private String reviewedAccountId;
    private String reviewedTopicId;
}
