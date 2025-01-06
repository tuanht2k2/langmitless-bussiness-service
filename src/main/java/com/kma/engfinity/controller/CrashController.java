package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditCrashRequest;
import com.kma.engfinity.DTO.request.StatisticsRequest;
import com.kma.engfinity.service.AccountService;
import com.kma.engfinity.service.CommonService;
import com.kma.engfinity.service.CrashService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/crashes")
public class CrashController {
    @Autowired
    CommonService commonService;

    @Autowired
    CrashService crashService;

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody EditCrashRequest request, HttpServletRequest httpServletRequest) {
        request.setIp(commonService.getClientIpAddress(httpServletRequest));
        return crashService.create(request);
    }

    @PostMapping("statistics")
    public ResponseEntity<?> statistics (@RequestBody StatisticsRequest request) {
        return crashService.statistics(request);
    }

    @GetMapping
    public ResponseEntity<?> test () throws InterruptedException {
        return crashService.test();
    }

}
