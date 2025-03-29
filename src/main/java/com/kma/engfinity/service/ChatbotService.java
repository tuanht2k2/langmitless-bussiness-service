package com.kma.engfinity.service;

import com.kma.common.dto.request.EditMessageRequest;
import com.kma.common.dto.response.Response;
import com.kma.common.entity.Account;
import com.kma.engfinity.service.interfaces.AiServiceProxy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatbotService {
    @Resource
    AiServiceProxy aiServiceProxy;

    public Response<Object> ask (String message) {
        try {
            return aiServiceProxy.ask(message);
        } catch (Exception e) {
            log.info(e.getMessage());
            return Response.getResponse(500, e.getMessage());
        }
    }
}
