package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EReviewType;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewResponse {
    private String id;
    private Integer rating;
    private String content;
    private PublicAccountResponse createdBy;
    private Date createdAt;
    private EReviewType type;

}
