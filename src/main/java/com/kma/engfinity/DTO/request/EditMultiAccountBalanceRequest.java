package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class EditMultiAccountBalanceRequest {
    private List<String> senderIds;
    private List<String> receiverIds;
    private Long balance;
}
