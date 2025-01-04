package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.WebRTCDto;
import com.kma.engfinity.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @MessageMapping("/rooms/video-call")
    public ResponseEntity<?> sendWebRTCData(@Payload WebRTCDto request) {
        return roomService.sendWebRTCData(request);
    }
}
