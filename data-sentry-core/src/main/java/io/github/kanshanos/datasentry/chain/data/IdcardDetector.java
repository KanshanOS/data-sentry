package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.util.IdcardUtil;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 身份证号码
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class IdcardDetector extends AbstractSensitiveDataDetector {
    private static final DetectorConfig CONFIG = new DetectorConfig("idcard", 10, 18);

    public IdcardDetector() {
        super(CONFIG);
    }

    @Override
    protected SensitiveDataItem doDetect(String name, String data) {
        return IdcardUtil.isValidCard(data) ? new SensitiveDataItem(CONFIG.getType(), name, data) : null;
    }
}
