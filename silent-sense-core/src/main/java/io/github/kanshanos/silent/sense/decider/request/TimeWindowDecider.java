package io.github.kanshanos.silent.sense.decider.request;

import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

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
public class TimeWindowDecider extends AbstractSenseRequestDecider {

    // key = 请求路径#method，value = 上次检测时间戳（秒）
    private final Map<String, Long> windowCache = new ConcurrentHashMap<>();

    private final long timeWindowSeconds;

    public TimeWindowDecider(SilentSenseProperties properties) {
        super(properties);
        this.timeWindowSeconds = properties.getTimeWindowHitIntervalSeconds(); // 例如 60 秒
    }

    @Override
    protected boolean check(HttpServletRequest request, HandlerMethod handler) {
        String key = key(request);
        long now = Instant.now().getEpochSecond();
        Long last = windowCache.get(key);

        if (last == null || now - last > timeWindowSeconds) {
            windowCache.put(key, now);
            return true;
        }
        return false;
    }

    private String key(HttpServletRequest request) {
        String method = request.getMethod();
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return method + "#" + pattern;
    }
}
