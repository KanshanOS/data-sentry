package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

/**
 * 抽象的 AbstractSenseDataChain
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

    public AbstractSensitiveDataDetector next(SensitiveDataDetector next) {
        this.next = next;
        return (AbstractSensitiveDataDetector) next;
    }

    @Override
    public SensitiveDataItem process( String name, String data) {
        SensitiveDataItem item = detect(name, data);
        if (item != null) {
            return item; // 检测到敏感数据，停止链式处理
        }
        return next != null ? next.process(name, data) : null;
    }

    protected abstract SensitiveDataItem detect( String name, String data);
}
