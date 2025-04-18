package io.github.kanshanos.silent.sense.chain.data;

/**
 * 敏感数据分析决策接口
 *
 * @author Kanshan
 * @since 2025/4/18 15:09
 */
public interface SenseDataChain {

    boolean process(String name, String data);
}
