package io.github.kanshanos.datasentry.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class SentryJsonGenerator extends JsonGeneratorDelegate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String fieldName;
    private SerializableString _fieldName;
    private final SensitiveDataDetector dataChain;


    public SentryJsonGenerator(JsonGenerator d, SensitiveDataDetector dataChain) {
        super(d);
        this.dataChain = dataChain;
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        this.fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        this._fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeString(String text) throws IOException {
        try {
            String name = StringUtils.firstNonBlank(fieldName, _fieldName.getValue());
            SensitiveDataItem sensitiveDataItem = this.dataChain.detect(name, text);
            if (Objects.nonNull(sensitiveDataItem)) {
                SentryContextHolder.addSensitiveData(sensitiveDataItem);
            }
        } catch (Exception e) {
            logger.error("writeString error", e);
        }
        super.writeString(text);
    }
}
