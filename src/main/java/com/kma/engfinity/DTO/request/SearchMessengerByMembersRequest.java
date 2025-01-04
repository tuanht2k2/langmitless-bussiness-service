package com.kma.engfinity.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchMessengerByMembersRequest {
    private List<String> members;
}
