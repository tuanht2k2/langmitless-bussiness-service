package com.kma.engfinity.DTO.request;

import com.kma.engfinity.DTO.response.PublicAccountResponse;
import com.kma.engfinity.enums.EMessageType;
import lombok.Data;

import java.util.Date;

@Data
public class EditMessageRequest {
    private String id;
    private String messenger;
    private String content;
    private EMessageType type;
}
