package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String id;
    private String messengerId;
    private Date createdAt;
    private PublicAccountResponse createdBy;
    private String content;
    private String fileUrl;
}
