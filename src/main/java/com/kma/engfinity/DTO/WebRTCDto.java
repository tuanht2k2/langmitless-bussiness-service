package com.kma.engfinity.DTO;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WebRTCDto {
    private String roomId;
    private String createdBy;
    private String type; // offer | answer | candidate
    private String sdp;
    private String candidate;
    private String sdpMid;
    private Integer sdpMLineIndex;
}
