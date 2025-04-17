package com.kma.engfinity.service.interfaces;

import com.kma.common.dto.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
@Component
public interface AiServiceProxy {
    @PostMapping("chatbot/ask")
    Response<Object> ask(@RequestBody String input);
}
