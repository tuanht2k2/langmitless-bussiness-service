package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class EditRoomRequest {
    private String id;
    private Integer expectedCallDuration;
    private Integer actualCallDuration;
    private List<String> members;
    private String hireId;
}
