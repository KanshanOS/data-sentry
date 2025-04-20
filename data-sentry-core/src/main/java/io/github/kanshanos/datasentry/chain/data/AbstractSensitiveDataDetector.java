package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 抽象敏感数据检测器，提供链式检测逻辑
 *
 * @author Kanshan
 * @since 2025/4/18 15:13
 */
public abstract class AbstractSensitiveDataDetector implements SensitiveDataDetector {

    private SensitiveDataDetector next;

    /**
     * 脱敏标记
     */
    protected static final String MASK_FLAG = "**";

    protected final DetectorConfig config;

    protected AbstractSensitiveDataDetector(DetectorConfig config) {
        this.config = config;
    }

    public AbstractSensitiveDataDetector next(SensitiveDataDetector next) {
        this.next = next;
        return (AbstractSensitiveDataDetector) next;
    }

    @Override
    public SensitiveDataItem detect(String name, String data) {
        if (!validateCommon(data)) {
            return next != null ? next.detect(name, data) : null;
        }

        SensitiveDataItem item = doDetect(name, data);
        if (item != null) {
            return item;
        }

        return next != null ? next.detect(name, data) : null;
    }

    protected boolean validateCommon(String data) {
        return data != null
                && data.length() >= config.getMinLength()
                && data.length() <= config.getMaxLength()
                && !StringUtils.contains(data, MASK_FLAG);
    }

    protected abstract SensitiveDataItem doDetect(String name, String data);

}
