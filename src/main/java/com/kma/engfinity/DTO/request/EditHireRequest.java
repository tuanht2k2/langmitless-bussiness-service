package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EHireStatus;
import lombok.Data;

@Data
public class EditHireRequest {
    private String id;
    private String teacherId;
    private Integer totalTime;
    private EHireStatus status;
}
