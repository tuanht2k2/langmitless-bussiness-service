package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class SearchCrashRequest {
    private Date from;
    private Date to;
}
