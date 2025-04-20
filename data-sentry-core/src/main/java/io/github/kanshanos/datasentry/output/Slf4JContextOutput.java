package io.github.kanshanos.datasentry.output;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * Slf4jOutput
 *
 * @author Kanshan
 * @since 2025/4/18 17:05
 */
@Slf4j
public class Slf4JContextOutput implements ContextOutput {
    @Override
    public void outputContext(SentryDataContext context) {
        log.info("Sentry Data URI :[{}] {}, Senses List : {}",
                context.getRequest().getMethod(),
                context.getRequest().getPattern(),
                context.getSensitiveData().stream().map(SensitiveDataItem::format).collect(Collectors.joining(",")));
    }

    @Override
    public void error(String msg, Throwable t) {
        log.error(msg, t);
    }
}
