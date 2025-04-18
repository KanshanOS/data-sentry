package io.github.kanshanos.silent.sense.decider.request;

import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 采样率
 *
 * @author Kanshan
 * @since 2025/4/18 14:34
 */
public class SamplingRateDecider extends AbstractSenseRequestDecider {

    /**
     * 采样率
     */
    private final double samplingRate;

    public SamplingRateDecider(SilentSenseProperties properties) {
        super(properties);
        this.samplingRate = properties.getSamplingRate();
    }

    @Override
    protected boolean check(HttpServletRequest request, HandlerMethod handler) {
        // 在 0.0 ~ 1.0 之间生成一个 double，若小于采样率则命中
        return ThreadLocalRandom.current().nextDouble() < samplingRate;
    }
}
