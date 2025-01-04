package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EVideoCallStatus;
import lombok.Data;

@Data
public class EditVideoCallRequest {
    private String messengerId;
    private EVideoCallStatus status;
    private String sdp;
    private String candidate;
}
