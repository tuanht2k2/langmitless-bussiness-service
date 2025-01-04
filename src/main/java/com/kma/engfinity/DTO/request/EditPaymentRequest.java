package com.kma.engfinity.DTO.request;

import com.kma.engfinity.entity.Account;
import com.kma.engfinity.enums.EPaymentStatus;
import com.kma.engfinity.enums.EPaymentType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EditPaymentRequest {
    private String id;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String receiver;
    private EPaymentType type;
    private Long amount;
    private String description;
    private EPaymentStatus status;
}
