package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于避免在指定时间窗口内对已检测到敏感数据的请求重复检测
 *
 * @author Kanshan
 * @since 2025/4/19 21:23
 */
public class SensitiveDataCacheFilter extends AbstractRequestFilterChain {

    public final Map<String, Long> cache = new ConcurrentHashMap<>();

    /**
     * 缓存有效期（秒），在此期间跳过重复检测
     */
    private final long cacheExpirationSeconds;

    public SensitiveDataCacheFilter(DataSentryProperties properties) {
        super(properties);
        cacheExpirationSeconds = properties.getCacheExpirationSeconds();
    }

    @Override
    public boolean filter(HttpServletRequest request) {
        String key = SentryContextHolder.getRequestHandler().key();
        long now = Instant.now().getEpochSecond();
        Long last = cache.get(key);

        boolean recheck = last == null || now - last > cacheExpirationSeconds;
        return recheck && super.filter(request);
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
