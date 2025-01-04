package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.ERole;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchAccountRequest extends CommonSearchRequest{
    private ERole role;
}
