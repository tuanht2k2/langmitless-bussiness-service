package com.kma.engfinity.DTO.response;

import lombok.Data;

@Data
public class CreateMomoPaymentResponse {
    private String partnerCode;
    private String orderId;
    private String requestId;
    private Long amount;
    private Long responseTime;
    private String message;
    private Integer resultCode;
    private String payUrl;
    private String shortLink;
}
