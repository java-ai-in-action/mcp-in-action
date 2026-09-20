package com.javaai.mcp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简单令牌桶限流：按 userId 每分钟 N 次
 *
 * 生产环境建议换成 Resilience4j RateLimiter 或 Sentinel，
 * 这里用最小实现演示"限流挂在 MCP 工具调用上"这个思路。
 */
@Component
public class RateLimiter {

    private final int permitsPerMinute;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(
            @Value("${app.mcp.rate-limit.permits-per-minute:60}") int permitsPerMinute) {
        this.permitsPerMinute = permitsPerMinute;
    }

    /** 返回 true 表示放行，false 表示被限流 */
    public boolean tryAcquire(String userId) {
        return buckets.computeIfAbsent(userId, k -> new Bucket())
                .tryAcquire(permitsPerMinute);
    }

    private static final class Bucket {
        private long windowStart = System.currentTimeMillis();
        private final AtomicInteger count = new AtomicInteger();

        synchronized boolean tryAcquire(int max) {
            long now = System.currentTimeMillis();
            if (now - windowStart > 60_000L) {
                windowStart = now;
                count.set(0);
            }
            return count.incrementAndGet() <= max;
        }
    }
}
