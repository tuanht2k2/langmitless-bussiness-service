package com.kma.engfinity.DTO.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private Integer totalRecords;
    private Integer recordSize;
    private List<T> list;
}
