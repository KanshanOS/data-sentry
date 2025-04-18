package io.github.kanshanos.silent.sense.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kanshanos.silent.sense.chain.data.AbstractSenseDataChain;
import io.github.kanshanos.silent.sense.chain.request.AbstractSenseRequestChain;
import io.github.kanshanos.silent.sense.core.SenseJacksonHttpMessageConverter;
import io.github.kanshanos.silent.sense.chain.data.BankCardChain;
import io.github.kanshanos.silent.sense.chain.data.ChineseAddressChain;
import io.github.kanshanos.silent.sense.chain.data.ChineseNameChain;
import io.github.kanshanos.silent.sense.chain.data.EmailChain;
import io.github.kanshanos.silent.sense.chain.data.FixedPhoneChain;
import io.github.kanshanos.silent.sense.chain.data.IdcardChain;
import io.github.kanshanos.silent.sense.chain.data.MobileChain;
import io.github.kanshanos.silent.sense.chain.data.SenseDataChain;
import io.github.kanshanos.silent.sense.chain.request.RequestURIWhitelistChain;
import io.github.kanshanos.silent.sense.chain.request.SamplingRateChain;
import io.github.kanshanos.silent.sense.chain.request.SenseRequestChain;
import io.github.kanshanos.silent.sense.chain.request.TimeWindowChain;
import io.github.kanshanos.silent.sense.interceptor.SenseInterceptor;
import io.github.kanshanos.silent.sense.output.Output;
import io.github.kanshanos.silent.sense.output.Slf4jOutput;
import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自动配置类
 *
 * @author Kanshan
 * @since 2025/4/18 10:55
 */
@Configuration
@EnableConfigurationProperties(SilentSenseProperties.class)
public class SilentSenseAutoConfiguration implements WebMvcConfigurer {

    private final SilentSenseProperties properties;

    public SilentSenseAutoConfiguration(SilentSenseProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverter<Object> loggingJacksonConverter(ObjectMapper objectMapper,
                                                                SenseRequestChain senseRequestChain,
                                                                SenseDataChain senseDataChain,
                                                                Output output) {
        return new SenseJacksonHttpMessageConverter(objectMapper, senseRequestChain, senseDataChain);
    }

    @Bean
    @ConditionalOnMissingBean
    public SenseRequestChain senseRequestChain() {
        AbstractSenseRequestChain head = new SamplingRateChain(properties);
        head.next(new RequestURIWhitelistChain(properties))
                .next(new TimeWindowChain(properties));
        return head;
    }

    @Bean
    @ConditionalOnMissingBean
    public SenseDataChain senseDataChain() {
        AbstractSenseDataChain head = new MobileChain();
        head.next(new IdcardChain())
                .next(new FixedPhoneChain())
                .next(new EmailChain())
                .next(new BankCardChain())
                .next(new ChineseAddressChain())
                .next(new ChineseNameChain());
        return head;
    }

    @Bean
    @ConditionalOnMissingBean
    public Output output() {
        return new Slf4jOutput();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!properties.isEnabled()) {
            return;
        }
        registry.addInterceptor(new SenseInterceptor(output())).addPathPatterns("/**");
    }
}
