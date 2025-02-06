package com.kma.engfinity.DTO.request;

import com.kma.common.enums.EGender;
import com.kma.common.enums.ERole;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EditAccountRequest {
    private String id;
    private String email;
    private String phoneNumber;
    private String password;
    private String profileImage;
    private String name;
    private Long balance;
    private ERole role;
    private EGender gender;
    private String address;
    private String identification;
    private String fullName;
}
