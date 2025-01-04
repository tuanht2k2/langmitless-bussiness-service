package com.kma.engfinity.controller;

import com.kma.engfinity.DTO.request.EditPaymentRequest;
import com.kma.engfinity.DTO.response.MomoPaymentResultResponse;
import com.kma.engfinity.service.MomoPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment/momo")
public class PaymentController {
    @Autowired
    MomoPaymentService momoPaymentService;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody EditPaymentRequest request) {
        return momoPaymentService.create(request);
    }

    @PostMapping("result")
    public void create(@RequestBody MomoPaymentResultResponse response) {
        momoPaymentService.handlePaymentResult(response);
    }
}
