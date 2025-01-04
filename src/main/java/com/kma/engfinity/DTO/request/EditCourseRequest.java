package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class EditCourseRequest {
    private String id;
    private String name;
    private String language;
    private String description;
    private Long cost;
}
