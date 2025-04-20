package io.github.kanshanos.datasentry.output;

import io.github.kanshanos.datasentry.context.SentryDataContext;

/**
 * 监控信息输出接口
 *
 * @author Kanshan
 * @since 2025/4/18 16:58
 */
public interface ContextOutput {

    void outputContext(SentryDataContext context);

    void error(String msg, Throwable t);
}
