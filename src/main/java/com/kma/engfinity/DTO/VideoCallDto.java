package com.kma.engfinity.DTO;

import lombok.Data;

@Data
public class VideoCallDto {
    private String messengerId;
    private String senderId;
    private WebRTCDto webRTC;
}
