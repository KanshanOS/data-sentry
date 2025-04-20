package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.util.PhoneUtil;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 固定电话
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class FixedPhoneDetector extends AbstractSensitiveDataDetector {
    private static final DetectorConfig CONFIG = new DetectorConfig("fixed_phone", 6, 15);

    public FixedPhoneDetector() {
        super(CONFIG);
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return PhoneUtil.isTel(data) ? new SensitiveDataItem(config.getType(), name, data) : null;
    }
}
