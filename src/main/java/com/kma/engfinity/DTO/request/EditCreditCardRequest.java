package com.kma.engfinity.DTO.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditCreditCardRequest {
    private String cardNumber;
    private String bank;
    private String accountId;
    private MultipartFile qrImage;
}
