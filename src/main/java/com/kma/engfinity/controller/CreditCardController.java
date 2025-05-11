package com.kma.engfinity.controller;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.CommonGetDataRequest;
import com.kma.engfinity.DTO.request.EditCreditCardRequest;
import com.kma.engfinity.service.CreditCardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/business/credit-cards")
public class CreditCardController {
    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping("create")
    public Response<Object> create (@ModelAttribute EditCreditCardRequest request) {
        return creditCardService.create(request);
    }

    @PostMapping("update")
    public Response<Object> update (@ModelAttribute EditCreditCardRequest request) {
        return creditCardService.update(request);
    }

    @PostMapping("details")
    public Response<Object> details () {
        return creditCardService.details();
    }
}
