package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @MessageMapping("/messages")
    public void create (@Payload EditMessageRequest request) throws Exception {
        messageService.create(request);
    }
}
