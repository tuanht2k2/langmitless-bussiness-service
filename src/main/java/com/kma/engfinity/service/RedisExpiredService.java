package com.kma.engfinity.service;

import com.kma.engfinity.service.interfaces.RedisExpiredServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class RedisExpiredService implements RedisExpiredServiceInterface {
    private final HireService hireService;

    public RedisExpiredService(HireService hireService) {
        this.hireService = hireService;
    }

    @Override
    public void checkMissedCalls(String expired) {
        hireService.checkMissedCalls(expired);
    }
}
