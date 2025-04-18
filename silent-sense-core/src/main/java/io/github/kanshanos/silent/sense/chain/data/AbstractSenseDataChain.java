package io.github.kanshanos.silent.sense.chain.data;

/**
 * 抽象的 SenseAnalyticsDecider
 *
 * @author Kanshan
 * @since 2025/4/18 15:13
 */
public abstract class AbstractSenseDataChain implements SenseDataChain {

    private SenseDataChain next;

    public AbstractSenseDataChain next(SenseDataChain next) {
        this.next = next;
        return (AbstractSenseDataChain) next;
    }

    @Override
    public boolean process(String name, String data) {
        if (match(name, data)) {
            return false; // 拦截住就直接返回 false，不继续判断后续节点
        }
        return next == null || next.process(name, data);
    }

    protected abstract boolean match(String name, String data);
}
