package com.kma.engfinity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessengerResponse {
    private String id;
    private String name;
    private String image;
    private String type;
    private Date updatedAt;
    private Date createdAt;
    private List<MessengerMemberResponse> members;
    private List<MessageResponse> messages;
}
