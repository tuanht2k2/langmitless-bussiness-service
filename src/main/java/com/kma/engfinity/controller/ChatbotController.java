package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.service.ChatbotService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("business/chatbot")
public class ChatbotController {
    @Resource
    ChatbotService chatbotService;

//    @PostMapping("chatbot-listener")
//    public Response<Object> chatbotListener(Object messageObj) {
//        return chatbotService.chatbotListener(messageObj);
//    }
}
