package io.github.kanshanos.silent.sense.output;

import io.github.kanshanos.silent.sense.context.SilentSense;

/**
 * 监控信息输出接口
 *
 * @author Kanshan
 * @since 2025/4/18 16:58
 */
public interface Output {

    void output(SilentSense sense);
}
