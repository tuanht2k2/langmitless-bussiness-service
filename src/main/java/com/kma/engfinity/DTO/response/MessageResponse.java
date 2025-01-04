package com.kma.engfinity.DTO.response;

import lombok.Data;

import java.util.Date;

@Data
public class MessageResponse {
    private String id;
    private String messenger;
    private Date createdAt;
    private PublicAccountResponse createdBy;
    private String content;
}
