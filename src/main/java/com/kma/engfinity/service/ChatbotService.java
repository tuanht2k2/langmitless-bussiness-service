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
    SimpMessagingTemplate messagingTemplate;

    @Resource
    AuthService authService;

//    public Response<Object> sendMessage (EditMessageRequest request) {
//        try {
//            Account account = authService.getCurrentAccount();
//            String destination = "/topic/messengers/" + account.getId() + "/messages";
//            messagingTemplate.convertAndSend("/topic/chat", input);
//        } catch (Exception e) {
//            log.info(e.getMessage());
//            return Response.getResponse(500, e.getMessage());
//        }
//    }
}
