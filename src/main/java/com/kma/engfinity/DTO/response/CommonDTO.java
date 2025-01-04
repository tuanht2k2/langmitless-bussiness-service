package com.kma.engfinity.DTO.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommonDTO {
    private Date createdAt;
    private Date updatedAt;
    private PublicAccountResponse createdBy;
    private PublicAccountResponse updatedBy;
}
