package io.github.kanshanos.datasentry.interceptor;


import io.github.kanshanos.datasentry.chain.request.RequestFilter;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.output.ContextOutput;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * @author Kanshan
 * @since 2025/4/18 11:13
 */
public class SentryInterceptor implements HandlerInterceptor {

    private final ContextOutput contextOutput;

    private final RequestFilter requestFilter;


    public SentryInterceptor(RequestFilter requestFilter, ContextOutput contextOutput) {
        this.requestFilter = requestFilter;
        this.contextOutput = contextOutput;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String method = request.getMethod();
            String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

            SentryContextHolder.setRequest(method, pattern);

            // 提前执行过滤链，决定是否需要敏感数据检测
            boolean shouldDetect = requestFilter.filter(SentryContextHolder.getRequest());
            SentryContextHolder.shouldDetect(shouldDetect);
            if (shouldDetect) {
                SentryContextHolder.shouldDetect(true);
            }
            return true;
        } catch (Exception e) {
            SentryContextHolder.clear();
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            SentryDataContext context = SentryContextHolder.getContext();
            if (context.isShouldDetect()) {
                requestFilter.handleContext(context);
            }

            if (context.isSensitiveDataDetected()) {
                contextOutput.outputContext(context);
            }
        } finally {
            SentryContextHolder.clear(); // 确保在任何情况下都清理 ThreadLocal
        }
    }
}
