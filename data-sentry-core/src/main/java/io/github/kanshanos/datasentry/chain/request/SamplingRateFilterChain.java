package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.properties.DataSentryProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 采样率
 *
 * @author Kanshan
 * @since 2025/4/18 14:34
 */
public class SamplingRateFilterChain extends AbstractRequestFilterChain {

    /**
     * 采样率
     */
    private final double samplingRate;

    public SamplingRateFilterChain(DataSentryProperties properties) {
        super(properties);
        this.samplingRate = properties.getSamplingRate();
    }

    @Override
    protected boolean filter(HttpServletRequest request) {
        // 在 0.0 ~ 1.0 之间生成一个 double，若小于采样率则命中
        return ThreadLocalRandom.current().nextDouble() < samplingRate;
    }
}
