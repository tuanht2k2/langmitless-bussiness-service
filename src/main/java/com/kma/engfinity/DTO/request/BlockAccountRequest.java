package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class BlockAccountRequest {
    private String accountId;
    private Date blockUntil;
}
