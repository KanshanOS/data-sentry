package io.github.kanshanos.silent.sense.chain.request;

import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractSenseRequestChain implements SenseRequestChain {
    private final SilentSenseProperties properties;
    private SenseRequestChain next;

    public AbstractSenseRequestChain(SilentSenseProperties properties) {
        this.properties = properties;
    }

    public AbstractSenseRequestChain next(SenseRequestChain next) {
        this.next = next;
        return (AbstractSenseRequestChain) next;
    }

    @Override
    public boolean process(HttpServletRequest request, HandlerMethod handler) {
        if (!check(request, handler)) {
            return false; // 拦截住就直接返回 false，不继续判断后续节点
        }
        return next == null || next.process(request, handler);
    }

    protected abstract boolean check(HttpServletRequest request, HandlerMethod handler);
}
