package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.ECrashType;
import lombok.Data;

@Data
public class EditCrashRequest {
    private String ip;
    private String error;
    private String createdBy;
    private ECrashType type;
}
