package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.CommonGetDataRequest;
import com.kma.engfinity.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/business/messengers")
public class MessengerController {
    @Autowired
    private MessengerService messengerService;

    @PostMapping("find-personal-messenger")
    public Response<Object> findPersonalMessenger (@RequestBody CommonGetDataRequest request) {
        return messengerService.findPersonalMessenger(request.getId());
    }

    @PostMapping("search")
    public Response<Object> search () {
        return messengerService.searchMessengers();
    }

    @PostMapping("details")
    public Response<Object> details (@RequestBody CommonGetDataRequest request) {
        return messengerService.details(request.getId());
    }
}
