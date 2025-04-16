package com.kma.engfinity.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchHireHistoryRequest {
    private String accountId;
    private String type; // HireType
}
