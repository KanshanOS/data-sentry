package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 中国姓名
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseNameChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^[\\u4e00-\\u9fa5]{2,5}(·[\\u4e00-\\u9fa5]{1,4})?$");
        if (matches) {
            SenseContextHolder.addSense("chinese_name", name, data);
        }
        return matches;
    }
}
