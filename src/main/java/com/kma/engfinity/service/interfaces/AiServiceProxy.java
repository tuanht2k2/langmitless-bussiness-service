package com.kma.engfinity.service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
@Component
public interface AiServiceProxy {
    @PostMapping("api/v1/ai/get-response")
    String getResponse(@RequestBody String input);
}
