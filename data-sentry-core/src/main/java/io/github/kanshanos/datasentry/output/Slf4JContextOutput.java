package io.github.kanshanos.datasentry.output;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * Slf4jOutput
 *
 * @author Kanshan
 * @since 2025/4/18 17:05
 */
public class Slf4JContextOutput implements ContextOutput {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void outputContext(SentryDataContext context) {
        logger.info("Sentry Data URI :[{}] {}, Senses List : {}",
                context.getRequest().getMethod(),
                context.getRequest().getPattern(),
                context.getSensitiveData().stream().map(SensitiveDataItem::format).collect(Collectors.joining(",")));
    }

}
