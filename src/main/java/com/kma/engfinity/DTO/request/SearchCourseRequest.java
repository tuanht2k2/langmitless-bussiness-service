package com.kma.engfinity.DTO.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchCourseRequest extends CommonSearchRequest{
    private String createdBy;
}
