package com.kma.engfinity.redis;

import com.kma.engfinity.service.RedisExpiredService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private final RedisExpiredService redisExpiredService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, RedisExpiredService redisExpiredService) {
        super(listenerContainer);
        this.redisExpiredService = redisExpiredService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String expired = message.toString();
            log.info("expired: {}", expired);
            redisExpiredService.checkMissedCalls(expired);
        } catch (Exception e) {
            log.error("An error occurred when checking key expired", e);
        }
    }
}
