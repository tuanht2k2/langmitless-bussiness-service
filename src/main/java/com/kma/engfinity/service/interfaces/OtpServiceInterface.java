package com.kma.engfinity.service.interfaces;

import com.kma.common.dto.response.Response;
import com.kma.engfinity.DTO.request.OtpRequest;

public interface OtpServiceInterface {
    Response<Object> getOtp();
    Response<Object> verifyOtp(OtpRequest request);
}
