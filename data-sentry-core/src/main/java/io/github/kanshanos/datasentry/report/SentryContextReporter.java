package io.github.kanshanos.datasentry.report;

import io.github.kanshanos.datasentry.context.SentryContextHolder;

/**
 * 上报给组件上下文
 *
 * @author Neo
 * @since 2025/4/19 11:35
 */
public class SentryContextReporter implements Reporter {

    @Override
    public void report(String type, String name, String data) {
        SentryContextHolder.addSensitiveData(type, name, data);
    }
}
