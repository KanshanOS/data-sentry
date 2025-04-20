package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指定时间内已经命中敏感信息的接口不再检测
 *
 * @author Kanshan
 * @since 2025/4/19 21:23
 */
public class SensitiveHitCacheChain extends AbstractRequestFilterChain {

    /**
     * 敏感信息命中缓存
     */
    public final Map<String, Long> cache = new ConcurrentHashMap<>();


    private final long sensitiveHitIntervalSeconds;

    public SensitiveHitCacheChain(DataSentryProperties properties) {
        super(properties);
        sensitiveHitIntervalSeconds = properties.getSensitiveHitIntervalSeconds();
    }

    @Override
    protected boolean filter(HttpServletRequest request) {
        String key = SentryContextHolder.getRequestHandler().key();
        Long lastHitTime = cache.get(key);
        // 检查是否在时间窗口内命中
        if (lastHitTime != null) {
            long now = Instant.now().getEpochSecond();
            return now - lastHitTime > sensitiveHitIntervalSeconds; // 在时间窗口内，跳过检测
        }
        return true;
    }

    @Override
    public void handleContext(SentryDataContext context) {
        if (context != null && SentryContextHolder.hit()) {
            String key = context.getRequest().key();
            cache.put(key, Instant.now().getEpochSecond());
        }
        super.handleContext(context);
    }
}
