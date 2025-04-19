package io.github.kanshanos.datasentry.interceptor;


import io.github.kanshanos.datasentry.cache.CacheManager;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.report.ContextOutput;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

/**
 * 拦截器
 *
 * @author Kanshan
 * @since 2025/4/18 11:13
 */
public class SentryInterceptor implements HandlerInterceptor {

    private final ContextOutput contextOutput;

    public SentryInterceptor(ContextOutput contextOutput) {
        this.contextOutput = contextOutput;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String method = request.getMethod();
            String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

            SentryContextHolder.setRequestHandler(method, pattern);
            return true;
        } catch (Exception e) {
            SentryContextHolder.clear();
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (SentryContextHolder.hit()) {
                CacheManager.SENSITIVE_DATA_HIT_CACHE.put(SentryContextHolder.getRequestHandler().key(), Instant.now().getEpochSecond());
                contextOutput.output(new SentryDataContext(SentryContextHolder.getRequestHandler(), SentryContextHolder.getSensitiveData()));
            }
        } finally {
            SentryContextHolder.clear(); // 确保在任何情况下都清理 ThreadLocal
        }
    }
}
