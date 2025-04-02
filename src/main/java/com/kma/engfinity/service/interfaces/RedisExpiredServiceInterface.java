package com.kma.engfinity.service.interfaces;

public interface RedisExpiredServiceInterface {
    void checkMissedCalls(String redisKey);
}
