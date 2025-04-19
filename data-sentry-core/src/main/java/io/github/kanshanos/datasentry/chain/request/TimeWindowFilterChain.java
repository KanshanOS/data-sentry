package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 时间窗口命中
 *
 * @author Kanshan
 * @since 2025/4/18 14:33
 */
public class TimeWindowFilterChain extends AbstractRequestFilterChain {

    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    private final long timeWindowHitIntervalSeconds;

    public TimeWindowFilterChain(DataSentryProperties properties) {
        super(properties);
        this.timeWindowHitIntervalSeconds = properties.getTimeWindowHitIntervalSeconds();
    }

    @Override
    protected boolean filter(HttpServletRequest request) {
        String key = SentryContextHolder.getRequestHandler().key();
        long now = Instant.now().getEpochSecond();
        Long last = cache.get(key);

        if (last == null || now - last > timeWindowHitIntervalSeconds) {
            cache.put(key, now);
            return true;
        }
        return false;
    }
}
