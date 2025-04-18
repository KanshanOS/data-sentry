package io.github.kanshanos.silent.sense.interceptor;


import io.github.kanshanos.silent.sense.context.SenseContextHolder;
import io.github.kanshanos.silent.sense.context.SenseItem;
import io.github.kanshanos.silent.sense.context.SilentSense;
import io.github.kanshanos.silent.sense.output.Output;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拦截器
 *
 * @author Kanshan
 * @since 2025/4/18 11:13
 */
public class SenseInterceptor implements HandlerInterceptor {

    private final Output output;

    public SenseInterceptor(Output output) {
        this.output = output;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            SenseContextHolder.setHandler((HandlerMethod) handler);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        List<SenseItem> senses = SenseContextHolder.getSenses();
        if (!CollectionUtils.isEmpty(senses)) {
            this.output.output(new SilentSense(SenseContextHolder.getHandler(), senses));
        }
        SenseContextHolder.clear();
    }
}
