package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限制请求在指定时间窗口内的处理频率
 *
 * @author Kanshan
 * @since 2025/4/18 14:33
 */
public class RequestRateWindowFilter extends AbstractRequestFilterChain {

    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    /**
     * 频率限制时间间隔（秒），在此期间阻止重复处理
     */
    private final long requestRateWindowIntervalSeconds;

    public RequestRateWindowFilter(DataSentryProperties properties) {
        super(properties);
        this.requestRateWindowIntervalSeconds = properties.getRequestRateWindowIntervalSeconds();
    }

    @Override
    public boolean filter(HttpServletRequest request) {
        String key = SentryContextHolder.getRequest().key();
        long now = Instant.now().getEpochSecond();
        Long last = cache.get(key);

        boolean recheck = last == null || now - last > requestRateWindowIntervalSeconds;
        return recheck && super.filter(request);
    }


    @Override
    public void handleContext(SentryDataContext context) {
        if (context.isProcessedByDetector()) {
            String key = context.getRequest().key();
            cache.put(key, Instant.now().getEpochSecond());
        }
        super.handleContext(context);
    }
}
