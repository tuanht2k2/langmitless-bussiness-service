package com.kma.engfinity.DTO.response;

import lombok.Data;

@Data
public class EditCreditCardResponse {
    private String cardNumber;
    private String accountId;
    private String qrImage;
}
