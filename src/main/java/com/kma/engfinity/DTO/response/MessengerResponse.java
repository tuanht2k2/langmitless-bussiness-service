package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EMessengerType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MessengerResponse {
    private String id;
    private List<PublicAccountResponse> members;
    private PublicAccountResponse createdBy;
    private Date createdAt;
    private Date updatedAt;
    private EMessengerType type;
}
