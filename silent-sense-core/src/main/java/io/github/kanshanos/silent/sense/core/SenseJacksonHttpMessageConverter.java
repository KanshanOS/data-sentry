package io.github.kanshanos.silent.sense.core;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.kanshanos.silent.sense.context.SenseContextHolder;
import io.github.kanshanos.silent.sense.chain.data.SenseDataChain;
import io.github.kanshanos.silent.sense.chain.request.SenseRequestChain;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;

public class SenseJacksonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private final SenseRequestChain requestChain;
    private final SenseDataChain dataChain;


    public SenseJacksonHttpMessageConverter(ObjectMapper objectMapper,
                                            SenseRequestChain requestChain,
                                            SenseDataChain dataChain) {
        super(objectMapper);
        this.requestChain = requestChain;
        this.dataChain = dataChain;
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());

        JsonGenerator generator;
        if (sense()) {
            generator = new SenseJsonGenerator(
                    getObjectMapper().getFactory().createGenerator(outputMessage.getBody(), encoding),
                    dataChain
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
    private boolean sense() {
        HttpServletRequest request = SenseContextHolder.getRequest();
        HandlerMethod handler = SenseContextHolder.getHandler();
        return this.requestChain.process(request, handler);
    }
}
