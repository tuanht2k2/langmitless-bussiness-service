package com.kma.engfinity.service;

import com.kma.engfinity.service.interfaces.AiServiceProxy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {
    @Resource
    AiServiceProxy aiServiceProxy;

    public String getResponse (String input) {
        return aiServiceProxy.getResponse(input);
    }
}
