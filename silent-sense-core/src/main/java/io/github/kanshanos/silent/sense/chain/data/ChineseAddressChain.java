package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 中国地址
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseAddressChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches(".*(省|市|自治区|自治州|县|区|镇|乡|村).*");
        if (matches) {
            SenseContextHolder.addSense("chinese_address", name, data);
        }
        return matches;
    }
}
