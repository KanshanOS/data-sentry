package io.github.kanshanos.datasentry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.jackson.SentryJacksonModule;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Kanshan
 * @since 2025/5/29 10:12
 */
@Configuration
public class SentryConfig {

    private final DataSentryProperties properties;
    private final ObjectMapper objectMapper;
    private final SensitiveDataDetector sensitiveDataDetector;

    public SentryConfig(DataSentryProperties properties, ObjectMapper objectMapper,
                        SensitiveDataDetector sensitiveDataDetector) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.sensitiveDataDetector = sensitiveDataDetector;
    }


    @PostConstruct
    public void init() {
        if (properties.isEnabled()) {
            objectMapper.registerModule(new SentryJacksonModule(sensitiveDataDetector));
        }
    }
}
