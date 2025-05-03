package com.kma.engfinity.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MessengerMemberResponse {
    private String id;
    private String name;
    private String profileImage;
}
