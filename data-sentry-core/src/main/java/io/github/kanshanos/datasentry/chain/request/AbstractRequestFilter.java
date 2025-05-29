package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

public abstract class AbstractRequestFilter implements RequestFilter {
    private final DataSentryProperties properties;
    private RequestFilter next;

    public AbstractRequestFilter(DataSentryProperties properties) {
        this.properties = properties;
    }

    public AbstractRequestFilter next(RequestFilter next) {
        this.next = next;
        return (AbstractRequestFilter) next;
    }

    @Override
    public boolean filter(Request request) {
        return next == null || next.filter(request);
    }

    @Override
    public void handleContext(SentryDataContext context) {
        if (next != null) {
            next.handleContext(context);
        }
    }
}
