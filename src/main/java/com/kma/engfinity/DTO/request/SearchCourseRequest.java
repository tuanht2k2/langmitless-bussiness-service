package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class SearchCourseRequest extends CommonSearchRequest{
    private String createdBy;
}
