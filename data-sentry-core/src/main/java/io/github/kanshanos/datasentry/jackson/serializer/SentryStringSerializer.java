package io.github.kanshanos.datasentry.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 敏感信息检测序列化器
 *
 * @author Kanshan
 * @since 2025/5/29 10:41
 */
public class SentryStringSerializer extends JsonSerializer<String> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SensitiveDataDetector dataDetector;

    public SentryStringSerializer(SensitiveDataDetector dataDetector) {
        this.dataDetector = dataDetector;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!SentryContextHolder.shouldDetect()) {
            gen.writeString(value);
        }

        String fieldName = gen.getOutputContext().getCurrentName();
        if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(value)) {
            gen.writeString(value);
        }

        try {
            SensitiveDataItem sensitiveDataItem = dataDetector.detect(fieldName, value);
            if (sensitiveDataItem != null) {
                SentryContextHolder.addSensitiveData(sensitiveDataItem);
                logger.debug("Detected sensitive data: {}", sensitiveDataItem.format());
            }
        } catch (Exception e) {
            logger.error("Error detecting sensitive data for field: {}", fieldName, e);
        }
        gen.writeString(value);
    }
}
