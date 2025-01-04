package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditCrashRequest;
import com.kma.engfinity.service.CommonService;
import com.kma.engfinity.service.CrashService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/crashes")
public class CrashController {
    @Autowired
    CommonService commonService;

    @Autowired
    CrashService crashService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditCrashRequest request, HttpServletRequest httpServletRequest) {
        request.setIp(commonService.getClientIpAddress(httpServletRequest));
        return crashService.create(request);
    }
}
