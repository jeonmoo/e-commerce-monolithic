package com.test.ecommerce.infra.health;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHealthIndicator implements HealthIndicator {

    private final StatefulRedisConnection<String, String> connection;

    @Override
    public Health health() {
        try {
            String pong = connection.sync().ping();
            if ("PONG".equalsIgnoreCase(pong)) {
                log.info("Initialize to redis connection");
                return Health.up().withDetail("redis", "available").build();
            } else {
                log.error("Failed to initialize redis connection");
                return Health.down().withDetail("redis", "unexpected ping response").build();
            }
        } catch (Exception e) {
            log.error("Failed to initialize redis connection: {}", e.getMessage());
            return Health.down(e).withDetail("redis", "unreachable").build();
        }
    }
}
