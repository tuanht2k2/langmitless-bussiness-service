package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class EditTopicRequest {
    private String id;
    private String description;
    private String courseId;
    private String tagId;
}
