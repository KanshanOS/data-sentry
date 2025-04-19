package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * 敏感数据分析决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 15:09
 */
public interface SensitiveDataDetector {

    boolean process(ContextOutput output, String name, String data);
}
