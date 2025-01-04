package com.kma.engfinity.DTO.request;

import lombok.Data;

@Data
public class CommonWebSocketEditRequest<E, S> {
    private E editRequest;
    private S searchRequest;
}
