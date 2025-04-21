package io.github.kanshanos;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * @author Neo
 * @since 2025/4/20 19:13
 */
public class ConsoleOutput implements ContextOutput {
    @Override
    public void outputContext(SentryDataContext context) {
        System.out.println("Sentry Data URI :[" + context.getRequest().getMethod() + "]" + context.getRequest().getPattern() + ", Senses List : " + context.getSensitiveData().stream().map(SensitiveDataItem::format).collect(java.util.stream.Collectors.joining(",")));
    }
}
