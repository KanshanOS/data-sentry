package io.github.kanshanos.silent.sense.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kanshanos.silent.sense.core.SenseJacksonHttpMessageConverter;
import io.github.kanshanos.silent.sense.decider.request.RequestURIWhitelistDecider;
import io.github.kanshanos.silent.sense.decider.request.SamplingRateDecider;
import io.github.kanshanos.silent.sense.decider.request.SenseRequestDecider;
import io.github.kanshanos.silent.sense.decider.request.TimeWindowDecider;
import io.github.kanshanos.silent.sense.interceptor.SenseInterceptor;
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
                                                                SenseRequestDecider senseRequestDecider) {
        return new SenseJacksonHttpMessageConverter(objectMapper, senseRequestDecider);
    }

    @Bean
    @ConditionalOnMissingBean
    public SenseRequestDecider senseRequestDecider() {
        SamplingRateDecider sampling = new SamplingRateDecider(properties);
        RequestURIWhitelistDecider whitelist = new RequestURIWhitelistDecider(properties);
        TimeWindowDecider timeWindow = new TimeWindowDecider(properties);

        sampling.linkWith(whitelist).linkWith(timeWindow);
        return sampling;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!properties.isEnabled()) {
            return;
        }
        registry.addInterceptor(new SenseInterceptor()).addPathPatterns("/**");
    }
}
