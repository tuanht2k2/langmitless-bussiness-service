package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditPaymentRequest;
import com.kma.engfinity.entity.Payment;
import com.kma.engfinity.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transfer")
public class TransferController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("create")
    Response<Object> createTransfer (@RequestBody EditPaymentRequest request) {
        Payment payment = paymentService.create(request);
        Boolean success = ObjectUtils.isEmpty(payment) ? false : true;
        return Response.getResponse(success ? 200 : 500, payment, success ? "Transfer created successfully" : "Transfer created failed");
    }
}
