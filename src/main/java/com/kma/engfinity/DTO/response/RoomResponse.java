package com.kma.engfinity.DTO.response;

import lombok.Data;

import java.util.Set;

@Data
public class RoomResponse {
    private String id;
    private Set<PublicAccountResponse> members;
    private Integer expectedCallDuration;
    private Integer actualCallDuration;

}
