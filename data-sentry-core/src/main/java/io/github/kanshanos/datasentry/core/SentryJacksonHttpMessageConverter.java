package io.github.kanshanos.datasentry.core;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.chain.request.RequestFilterChain;
import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.context.SentryContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class SentryJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RequestFilterChain requestChain;
    private final SensitiveDataDetector dataChain;



    public SentryJacksonHttpMessageConverter(ObjectMapper objectMapper,
                                             RequestFilterChain requestChain,
                                             SensitiveDataDetector dataChain) {
        super(objectMapper);
        this.requestChain = requestChain;
        this.dataChain = dataChain;
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator generator = createJsonGenerator(outputMessage, encoding);

        // 启用格式化输出（如果配置了）
        if (getObjectMapper().isEnabled(SerializationFeature.INDENT_OUTPUT)) {
            generator.useDefaultPrettyPrinter();
        }

        // 序列化对象
        getObjectMapper().writeValue(generator, object);
    }

    /**
     * 创建适当的 JsonGenerator，根据是否需要敏感数据检测选择实现。
     *
     * @param outputMessage HTTP 输出消息
     * @param encoding      JSON 编码格式
     * @return JsonGenerator 实例
     * @throws IOException 如果创建失败
     */
    private JsonGenerator createJsonGenerator(HttpOutputMessage outputMessage, JsonEncoding encoding)
            throws IOException {
        if (shouldDetectSensitiveData()) {
            logger.debug("Creating SentryJsonGenerator for sensitive data detection");
            return new SentryJsonGenerator(
                    getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding),
                    dataChain
            );
        }
        return getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding);
    }

    /**
     * 判断是否需要进行敏感数据检测
     */
    private boolean shouldDetectSensitiveData() {
        try {
            Request request = SentryContextHolder.getRequest();
            boolean shouldDetect = this.requestChain.filter(request);
            if (shouldDetect) {
                logger.debug("Request approved for sensitive data detection: {}", SentryContextHolder.getRequest().format());
                SentryContextHolder.processedByDetector(true);
            }
            return shouldDetect;
        } catch (Exception e) {
            logger.error("Failed to evaluate request for sensitive data detection: {}", SentryContextHolder.getRequest().format(), e);
            return false;
        }
    }
}
