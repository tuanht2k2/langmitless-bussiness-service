package com.kma.engfinity.DTO.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseResponse {
    private String id;
    private String name;
    private String description;
    private Long cost = 0L;
    private String language;
    private Date createdAt;
    private Date updatedAt;
    private PublicAccountResponse createdBy;
    private List<PublicAccountResponse> members;
    private List<TopicResponse> topics;
}
