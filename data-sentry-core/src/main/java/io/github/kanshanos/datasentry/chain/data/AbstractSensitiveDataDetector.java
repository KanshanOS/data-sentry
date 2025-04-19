package io.github.kanshanos.datasentry.chain.data;

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
    public boolean process(String name, String data) {
        if (detect(name, data)) {
            return false; // 拦截住就直接返回 false，不继续判断后续节点
        }
        return next == null || next.process(name, data);
    }

    protected abstract boolean detect(String name, String data);
}
