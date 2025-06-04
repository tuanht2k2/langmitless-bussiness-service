package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class EditAccountBlockedRequest {
    private String accountId;
    private Date blockedUntil;
}
