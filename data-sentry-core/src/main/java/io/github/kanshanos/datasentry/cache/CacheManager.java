package io.github.kanshanos.datasentry.cache;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Neo
 * @since 2025/4/19 21:30
 */
public class CacheManager {

    /**
     * 敏感信息命中缓存
     */
    public static final Map<String, Long> SENSITIVE_DATA_HIT_CACHE = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService CLEANER = Executors.newSingleThreadScheduledExecutor();

    static {
        CLEANER.scheduleAtFixedRate(() -> {
            long now = Instant.now().getEpochSecond();
            SENSITIVE_DATA_HIT_CACHE.entrySet().removeIf(entry -> now - entry.getValue() > 3600);
        }, 60, 60, TimeUnit.SECONDS);
    }
}
