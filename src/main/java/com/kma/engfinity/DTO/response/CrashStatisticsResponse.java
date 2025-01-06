package com.kma.engfinity.DTO.response;

import com.kma.engfinity.entity.Crash;
import com.kma.engfinity.entity.Request;
import lombok.Data;

import java.util.List;

@Data
public class CrashStatisticsResponse {
    private List<Crash> crashes;
    private List<Request> requests;
    private Integer totalUser;
}
