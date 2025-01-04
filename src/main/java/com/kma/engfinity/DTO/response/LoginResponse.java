package com.kma.engfinity.DTO.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private PrivateAccountResponse data;
}
