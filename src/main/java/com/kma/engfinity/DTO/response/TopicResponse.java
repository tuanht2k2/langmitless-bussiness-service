package com.kma.engfinity.DTO.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TopicResponse extends CommonDTO{
    private String id;
    private TagResponse tag;
    private String description;
}
