package com.kma.engfinity.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AiCourseDTO {
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
    private String createdByName;

    @JsonProperty("isMember")
    private boolean isMember;
}
