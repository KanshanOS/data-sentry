package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.lang.Validator;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 手机号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class MobileDetector extends AbstractSensitiveDataDetector {

    private static final DetectorConfig CONFIG = new DetectorConfig("mobile", 11, 14);

    public MobileDetector() {
        super(CONFIG);
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return Validator.isMobile(data) ? new SensitiveDataItem(CONFIG.getType(), name, data) : null;
    }
}
