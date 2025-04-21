package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.context.SentryDataContext;

/**
 * 请求处理决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 11:29
 */
public interface RequestFilterChain {
    /**
     * 过滤请求，决定是否需要处理
     *
     * @param request 请求
     * @return 是否需要处理
     */
    boolean filter(Request request);

    /**
     * 处理上下文数据，例如缓存回写
     *
     * @param context 敏感数据上下文
     */
    void handleContext(SentryDataContext context);
}
