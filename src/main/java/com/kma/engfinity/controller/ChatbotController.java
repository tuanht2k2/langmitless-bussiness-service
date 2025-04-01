package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.AskChatbotRequest;
import com.kma.engfinity.service.ChatbotService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/business/chatbot")
public class ChatbotController {
    @Resource
    ChatbotService chatbotService;

    @PostMapping("ask")
    public Response<Object> ask(@RequestBody AskChatbotRequest request) {
        return chatbotService.ask(request.getMessage());
    }
}
