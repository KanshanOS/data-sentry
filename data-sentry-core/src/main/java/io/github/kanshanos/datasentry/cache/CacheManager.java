package io.github.kanshanos.datasentry.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Neo
 * @since 2025/4/19 21:30
 */
public class CacheManager {

    /**
     * 敏感信息命中缓存
     */
    public static final Map<String, Long> SENSITIVE_DATA_HIT_CACHE = new ConcurrentHashMap<>();

}
