package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * 抽象的 AbstractSenseDataChain
 *
 * @author Kanshan
 * @since 2025/4/18 15:13
 */
public abstract class AbstractSensitiveDataDetector implements SensitiveDataDetector {

    private SensitiveDataDetector next;

    public AbstractSensitiveDataDetector next(SensitiveDataDetector next) {
        this.next = next;
        return (AbstractSensitiveDataDetector) next;
    }

    @Override
    public boolean process(ContextOutput output, String name, String data) {
        if (detect(output, name, data)) {
            return false; // 拦截住就直接返回 false，不继续判断后续节点
        }
        return next == null || next.process(output, name, data);
    }

    protected abstract boolean detect(ContextOutput output, String name, String data);
}
