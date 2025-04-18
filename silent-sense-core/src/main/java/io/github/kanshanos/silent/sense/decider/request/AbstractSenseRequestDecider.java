package io.github.kanshanos.silent.sense.decider.request;

import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractSenseRequestDecider implements SenseRequestDecider {
    private final SilentSenseProperties properties;
    private SenseRequestDecider next;

    public AbstractSenseRequestDecider(SilentSenseProperties properties) {
        this.properties = properties;
    }

    public AbstractSenseRequestDecider linkWith(SenseRequestDecider next) {
        this.next = next;
        return (AbstractSenseRequestDecider) next;
    }

    @Override
    public boolean shouldProcess(HttpServletRequest request, HandlerMethod handler) {
        if (!check(request, handler)) {
            return false; // 拦截住就直接返回 false，不继续判断后续节点
        }
        return next == null || next.shouldProcess(request, handler);
    }

    protected abstract boolean check(HttpServletRequest request, HandlerMethod handler);
}
