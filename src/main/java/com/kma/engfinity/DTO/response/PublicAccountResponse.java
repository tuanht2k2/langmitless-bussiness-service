package com.kma.engfinity.DTO.response;

import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EGender;
import com.kma.engfinity.enums.ERole;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class PublicAccountResponse {
    private String id;
    private String profileImage;
    private String name;
    private ERole role;
    private List<ReviewResponse> reviews;
    private EAccountStatus status;
    private Long cost;
}
