package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EGender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrivateAccountResponse extends PublicAccountResponse{
    private String email;
    private String phoneNumber;
    private Long balance;
    private String address;
    private EGender gender;
    private String fullName;
    private String identification;
}
