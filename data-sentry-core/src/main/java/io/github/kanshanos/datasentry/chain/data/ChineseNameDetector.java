package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.lang.Validator;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 中国姓名
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseNameDetector extends AbstractSensitiveDataDetector {
    private static final DetectorConfig CONFIG = new DetectorConfig("chinese_name", 2, 4);

    public ChineseNameDetector() {
        super(CONFIG);
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return Validator.isChineseName(data) ? new SensitiveDataItem(CONFIG.getType(), name, data) : null;

    }
}
