package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 身份证号码
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class IdcardChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("(^\\d{15}$)|(^\\d{17}([0-9Xx])$)");
        if (matches) {
            SenseContextHolder.addSense("idcard", name, data);
        }
        return matches;
    }
}
