package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EAccountStatus;
import lombok.Data;

@Data
public class EditAccountStatusRequest {
    private String id;
    private EAccountStatus status;
}
