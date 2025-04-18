package io.github.kanshanos.silent.sense.interceptor;


import io.github.kanshanos.silent.sense.context.SenseContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 拦截器
 *
 * @author Kanshan
 * @since 2025/4/18 11:13
 */
public class SenseInterceptor implements HandlerInterceptor {

    private static final AtomicLong COUNTER = new AtomicLong();


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
        SenseContextHolder.clear();
    }
}
