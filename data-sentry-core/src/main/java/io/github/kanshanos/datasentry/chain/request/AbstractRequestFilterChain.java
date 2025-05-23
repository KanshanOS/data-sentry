package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.context.SentryDataContext;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

public abstract class AbstractRequestFilterChain implements RequestFilterChain {
    private final DataSentryProperties properties;
    private RequestFilterChain next;

    public AbstractRequestFilterChain(DataSentryProperties properties) {
        this.properties = properties;
    }

    public AbstractRequestFilterChain next(RequestFilterChain next) {
        this.next = next;
        return (AbstractRequestFilterChain) next;
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
