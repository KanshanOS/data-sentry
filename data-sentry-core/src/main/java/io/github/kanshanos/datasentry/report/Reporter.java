package io.github.kanshanos.datasentry.report;

/**
 * 敏感数据上报接口
 *
 * @author Kanshan
 * @since 2025/4/19 11:31
 */
public interface Reporter {
    /**
     * 上报数据
     */
    void report(String type, String name, String data);
}
