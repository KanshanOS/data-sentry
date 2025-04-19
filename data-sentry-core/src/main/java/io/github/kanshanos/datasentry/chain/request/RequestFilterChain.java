package io.github.kanshanos.datasentry.chain.request;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求处理决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 11:29
 */
public interface RequestFilterChain {
    /**
     * 是否需要处理
     *
     * @param request 请求
     * @return 是否需要处理
     */
    boolean process(HttpServletRequest request);
}
