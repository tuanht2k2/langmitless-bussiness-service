package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class EditRequestLogRequest {
    private String id;
    private String ip;
    private String createdBy;
    private Date createdAt;
}
