package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EPaymentStatus;
import com.kma.engfinity.enums.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
