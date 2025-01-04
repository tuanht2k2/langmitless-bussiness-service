package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class SearchTopicRequest extends CommonSearchRequest{
    private String createdBy;
}
