package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 敏感数据分析决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 15:09
 */
public interface SensitiveDataDetector {

    /**
     * 处理数据并返回检测到的敏感数据项
     *
     * @author Kanshan
     * @since 2025/4/20 11:07
     */
    SensitiveDataItem process(String name, String data);
}
