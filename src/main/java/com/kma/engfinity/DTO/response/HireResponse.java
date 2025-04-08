package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EHireStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class HireResponse {
    private String id;
    private Date createdAt;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse teacher;
    private Long cost;
    private Integer totalTime;
    private EHireStatus status;

    public HireResponse(String id, Long cost, Integer totalTime, EHireStatus status, Date createdAt) {
        this.id = id;
        this.cost = cost;
        this.totalTime = totalTime;
        this.status = status;
        this.createdAt = createdAt;
    }
}
