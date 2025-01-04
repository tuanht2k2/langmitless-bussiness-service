package com.kma.engfinity.DTO.request;

import com.kma.engfinity.enums.ETransferType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EditAccountBalanceRequest {
    private String id;
    private Long amount;
    private ETransferType type;
}
