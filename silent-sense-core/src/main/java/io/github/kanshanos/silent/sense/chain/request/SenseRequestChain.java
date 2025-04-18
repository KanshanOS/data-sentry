package io.github.kanshanos.silent.sense.chain.request;

import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求处理决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 11:29
 */
public interface SenseRequestChain {
    /**
     * 是否需要处理
     *
     * @param request 请求
     * @param handler 处理器
     * @return 是否需要处理
     */
    boolean process(HttpServletRequest request, HandlerMethod handler);
}
