package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.service.ChatbotService;
import jakarta.annotation.Resource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("business/chatbot")
public class ChatbotController {
    @Resource
    ChatbotService chatbotService;

    @MessageMapping("{id}")
    public Response<Object> ask(String message) {
        return chatbotService.ask(message);
    }
}
