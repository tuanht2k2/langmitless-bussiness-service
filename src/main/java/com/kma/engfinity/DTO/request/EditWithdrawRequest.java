package com.kma.engfinity.DTO.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class EditWithdrawRequest {
    private String id;
    private Long amount;
    private String description;
    private String status;
    private String adminNote;
    private MultipartFile image;
}
