package com.kma.engfinity.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSearchCourseRequest {
    private String name;
    private String language;
    private Integer minCost;
    private Integer maxCost;
    private Byte level;
}
