package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EHireStatus;
import lombok.Data;

import java.util.Date;

@Data
public class HireResponse {
    private String id;
    private Date createdAt;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse teacher;
    private Long cost;
    private Byte totalTime;
    private EHireStatus status;
}
