package com.kma.engfinity.service;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.service.interfaces.AiServiceProxy;
import jakarta.annotation.Resource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
    @Resource
    SimpMessagingTemplate messagingTemplate;

//    public Response<Object> sendMessage (Object input) {
//        try {
//
//            messagingTemplate.convertAndSend("/topic/chat", input);
//        } catch () {
//
//        }
//    }
}
