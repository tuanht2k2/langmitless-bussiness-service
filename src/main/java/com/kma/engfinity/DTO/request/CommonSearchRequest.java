package com.kma.engfinity.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonSearchRequest {
    private int page = 0;
    private int pageSize = 3;
    private String keyword;
    private String sortBy = "created_at";
    private String sortDir = "DESC"; // ASC | DESC
}
