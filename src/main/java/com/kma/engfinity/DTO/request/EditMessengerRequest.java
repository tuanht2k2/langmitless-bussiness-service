package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.EMessengerType;
import lombok.Data;

import java.util.List;

@Data
public class EditMessengerRequest {
    private String id;
    private EMessengerType type;
    private String name;
    private List<String> members;
}
