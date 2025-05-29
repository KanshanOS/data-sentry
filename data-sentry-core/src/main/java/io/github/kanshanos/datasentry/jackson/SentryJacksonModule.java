package io.github.kanshanos.datasentry.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.jackson.serializer.SentryStringSerializer;


public class SentryJacksonModule extends SimpleModule {

    public SentryJacksonModule(SensitiveDataDetector dataDetector) {
        super("SentryJacksonModule");
        addSerializer(String.class, new SentryStringSerializer(dataDetector));
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
    }
}
