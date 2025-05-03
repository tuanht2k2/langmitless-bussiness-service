package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.CommonGetDataRequest;
import com.kma.engfinity.DTO.request.EditMessageRequest;
import com.kma.engfinity.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/business/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping("create")
    public Response<Object> create (@ModelAttribute EditMessageRequest request) {
        return messageService.create(request);
    }
}
