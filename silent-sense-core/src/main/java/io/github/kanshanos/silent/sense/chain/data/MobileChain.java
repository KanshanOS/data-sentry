package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 手机号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class MobileChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^(\\d{3,4}-?)?\\d{7,8}$");
        if (matches) {
            SenseContextHolder.addSense("mobile", name, data);
        }
        return matches;
    }
}
