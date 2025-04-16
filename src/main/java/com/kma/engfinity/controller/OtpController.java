package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.OtpRequest;
import com.kma.engfinity.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {
    @Autowired
    private OtpService otpService;

    @PostMapping("get")
    public Response<Object> getOtp() {
        return otpService.getOtp();
    }

    @PostMapping("verify")
    public Response<Object> verifyOtp(@RequestBody OtpRequest request) {
        return otpService.verifyOtp(request);
    }
}
