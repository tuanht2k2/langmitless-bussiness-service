package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class EditCrashRequest {
    private String ip;
    private String error;
    private String createdBy;
}
