package io.github.kanshanos.datasentry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kanshanos.datasentry.chain.data.AbstractSensitiveDataDetector;
import io.github.kanshanos.datasentry.chain.data.BankCardDetector;
import io.github.kanshanos.datasentry.chain.data.ChineseAddressDetector;
import io.github.kanshanos.datasentry.chain.data.ChineseNameDetector;
import io.github.kanshanos.datasentry.chain.data.EmailDetector;
import io.github.kanshanos.datasentry.chain.data.FixedPhoneDetector;
import io.github.kanshanos.datasentry.chain.data.IdcardDetector;
import io.github.kanshanos.datasentry.chain.data.MobileDetector;
import io.github.kanshanos.datasentry.chain.data.SensitiveDataDetector;
import io.github.kanshanos.datasentry.chain.request.AbstractRequestFilterChain;
import io.github.kanshanos.datasentry.chain.request.RequestFilterChain;
import io.github.kanshanos.datasentry.chain.request.RequestRateWindowFilter;
import io.github.kanshanos.datasentry.chain.request.RequestURIWhitelistFilterChain;
import io.github.kanshanos.datasentry.chain.request.SamplingRateFilterChain;
import io.github.kanshanos.datasentry.chain.request.SensitiveDetectionHitWindowFilter;
import io.github.kanshanos.datasentry.core.SentryJacksonHttpMessageConverter;
import io.github.kanshanos.datasentry.interceptor.SentryInterceptor;
import io.github.kanshanos.datasentry.output.ContextOutput;
import io.github.kanshanos.datasentry.output.Slf4JContextOutput;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;
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
@EnableConfigurationProperties(DataSentryProperties.class)
public class DataSentryAutoConfiguration implements WebMvcConfigurer {

    private final DataSentryProperties properties;

    public DataSentryAutoConfiguration(DataSentryProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverter<Object> loggingJacksonConverter(ObjectMapper objectMapper,
                                                                RequestFilterChain requestFilterChain,
                                                                SensitiveDataDetector sensitiveDataDetector) {
        return new SentryJacksonHttpMessageConverter(
                objectMapper,
                requestFilterChain,
                sensitiveDataDetector
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestFilterChain requestFilterChain() {
        AbstractRequestFilterChain head = new SamplingRateFilterChain(properties);
        head.next(new RequestURIWhitelistFilterChain(properties))
                .next(new RequestRateWindowFilter(properties))
                .next(new SensitiveDetectionHitWindowFilter(properties));
        return head;
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveDataDetector sensitiveDataDetector() {
        AbstractSensitiveDataDetector head = new MobileDetector();
        head.next(new IdcardDetector())
                .next(new FixedPhoneDetector())
                .next(new EmailDetector())
                .next(new BankCardDetector())
                .next(new ChineseAddressDetector())
                .next(new ChineseNameDetector());
        return head;
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextOutput contextOutput() {
        return new Slf4JContextOutput();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!properties.isEnabled()) {
            return;
        }
        registry.addInterceptor(new SentryInterceptor(requestFilterChain(), contextOutput())).addPathPatterns("/**");
    }
}
