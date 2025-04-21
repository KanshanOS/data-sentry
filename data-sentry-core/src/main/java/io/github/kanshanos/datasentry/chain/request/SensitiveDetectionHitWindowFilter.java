package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于避免在指定时间窗口内对已检测到敏感数据的请求重复检测
 *
 * @author Kanshan
 * @since 2025/4/19 21:23
 */
public class SensitiveDetectionHitWindowFilter extends AbstractRequestFilterChain {

    public final Map<String, Long> cache = new ConcurrentHashMap<>();

    /**
     * 缓存有效期（秒），在此期间跳过重复检测
     */
    private final long sensitiveDetectionHitWindowIntervalSeconds;

    public SensitiveDetectionHitWindowFilter(DataSentryProperties properties) {
        super(properties);
        sensitiveDetectionHitWindowIntervalSeconds = properties.getSensitiveDetectionHitWindowIntervalSeconds();
    }

    @Override
    public boolean filter(Request request) {
        String key = request.key();
        long now = Instant.now().getEpochSecond();
        Long last = cache.get(key);

        boolean recheck = last == null || now - last > sensitiveDetectionHitWindowIntervalSeconds;
        return recheck && super.filter(request);
    }

    @Override
    public void handleContext(SentryDataContext context) {
        if (context.isSensitiveDataDetected()) {
            String key = context.getRequest().key();
            cache.put(key, Instant.now().getEpochSecond());
        }
        super.handleContext(context);
    }
}
