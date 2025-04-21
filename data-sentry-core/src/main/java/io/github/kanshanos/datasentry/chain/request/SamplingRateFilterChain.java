package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.context.Request;
import io.github.kanshanos.datasentry.properties.DataSentryProperties;

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
    public boolean filter(Request request) {
        // 在 0.0 ~ 1.0 之间生成一个 double，若小于采样率则命中
        boolean shouldProcess = ThreadLocalRandom.current().nextDouble() < samplingRate;
        return shouldProcess && super.filter(request);
    }
}
