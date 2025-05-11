package com.kma.engfinity.service.interfaces;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.EditCreditCardRequest;

public interface CreditCardServiceInterface {
    Response<Object> create(EditCreditCardRequest request);
    Response<Object> update(EditCreditCardRequest request);
    Response<Object> details();
}
