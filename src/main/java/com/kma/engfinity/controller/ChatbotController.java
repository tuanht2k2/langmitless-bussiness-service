package com.kma.engfinity.controller;

import com.kma.engfinity.service.ChatbotService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/chatbot")
public class ChatbotController {
    @Resource
    ChatbotService chatbotService;

    @PostMapping("get-response")
    public String getResponse(@RequestBody String message) {
        return chatbotService.getResponse(message);
    }
}
