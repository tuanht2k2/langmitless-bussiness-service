package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public void sendData(String destination, Object data) {
        try {
            log.info("Sending data to: {} - data: {} ", destination, objectMapper.writeValueAsString(data));
            messagingTemplate.convertAndSend(destination, data);
        } catch (Exception e) {
            log.error("An error occurred when sendData: {}", e.getMessage());
        }
    };
}
