package io.github.kanshanos.silent.sense.chain.request;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;
import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.web.method.HandlerMethod;

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
public class TimeWindowChain extends AbstractSenseRequestChain {

    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    private final long timeWindowHitIntervalSeconds;

    public TimeWindowChain(SilentSenseProperties properties) {
        super(properties);
        this.timeWindowHitIntervalSeconds = properties.getTimeWindowHitIntervalSeconds();
    }

    @Override
    protected boolean check(HttpServletRequest request, HandlerMethod handler) {
        String key = key(request);
        long now = Instant.now().getEpochSecond();
        Long last = cache.get(key);

        if (last == null || now - last > timeWindowHitIntervalSeconds) {
            cache.put(key, now);
            return true;
        }
        return false;
    }

    /**
     * 缓存 Key
     *
     * @author Kanshan
     * @since 2025/4/18 14:59
     */
    private String key(HttpServletRequest request) {
        String method = request.getMethod();
        String pattern = SenseContextHolder.bestMatchingPattern();
        return method + "#" + pattern;
    }
}
