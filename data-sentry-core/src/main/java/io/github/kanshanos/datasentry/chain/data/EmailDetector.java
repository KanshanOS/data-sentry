package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.lang.Validator;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 邮箱
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class EmailDetector extends AbstractSensitiveDataDetector {

    private static final DetectorConfig CONFIG = new DetectorConfig("email", 5, 30);

    public EmailDetector() {
        super(CONFIG);
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return Validator.isEmail(data) ? new SensitiveDataItem(CONFIG.getType(), name, data) : null;
    }
}
