package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.Date;

@Data
public class SearchRequestLogRequest {
    private Date from;
    private Date to;
}
