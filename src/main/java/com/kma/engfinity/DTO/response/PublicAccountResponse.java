package com.kma.engfinity.DTO.response;

import com.kma.engfinity.entity.Hire;
import com.kma.engfinity.enums.EAccountStatus;
import com.kma.engfinity.enums.EGender;
import com.kma.engfinity.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicAccountResponse {
    private String id;
    private String profileImage;
    private String name;
    private ERole role;
    private List<ReviewResponse> reviews;
    private EAccountStatus status;
    private Long cost;
    private String phoneNumber;
    private List<Hire> hireHistory;

    public PublicAccountResponse(String id, String name, String phoneNumber, String profileImage) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }
}
