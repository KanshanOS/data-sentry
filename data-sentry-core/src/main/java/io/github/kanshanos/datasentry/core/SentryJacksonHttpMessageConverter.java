package io.github.kanshanos.datasentry.core;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.chain.request.RequestFilterChain;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import io.github.kanshanos.datasentry.output.ContextOutput;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

public class SentryJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private final RequestFilterChain requestChain;
    private final SensitiveDataDetector dataChain;
    private final ContextOutput output;


    public SentryJacksonHttpMessageConverter(ObjectMapper objectMapper,
                                             RequestFilterChain requestChain,
                                             SensitiveDataDetector dataChain,
                                             ContextOutput output) {
        super(objectMapper);
        this.requestChain = requestChain;
        this.dataChain = dataChain;
        this.output = output;
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());

        JsonGenerator generator;
        if (this.shouldDetectSensitiveData()) {
            generator = new SentryJsonGenerator(
                    getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding),
                    dataChain,
                    output
            );
        } else {
            generator = getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);
        }

        if (getObjectMapper().isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            generator.useDefaultPrettyPrinter();
        }

        getObjectMapper().writeValue(generator, object);
    }

    /**
     * 判断是否需要使用 SenseJsonGenerator
     */
    private boolean shouldDetectSensitiveData() {
        try {
            HttpServletRequest request = SentryContextHolder.getRequest();
            return this.requestChain.filter(request);
        } catch (Exception e) {
            output.error("SenseJsonGenerator error", e);
            return false;
        }
    }
}
