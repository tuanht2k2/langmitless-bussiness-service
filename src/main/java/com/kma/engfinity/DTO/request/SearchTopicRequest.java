package com.kma.engfinity.DTO.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchTopicRequest extends CommonSearchRequest{
    private String createdBy;
}
