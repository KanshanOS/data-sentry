package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

import java.util.regex.Pattern;

/**
 * 基于正则表达式的检测器
 *
 * @author Kanshan
 * @since 2025/4/20
 */
public abstract class RegexBasedDetector extends AbstractSensitiveDataDetector {

    private final Pattern pattern;

    protected RegexBasedDetector(DetectorConfig config, Pattern pattern) {
        super(config);
        this.pattern = pattern;
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return pattern.matcher(data).matches() ? new SensitiveDataItem(config.getType(), name, data) : null;
    }
}
