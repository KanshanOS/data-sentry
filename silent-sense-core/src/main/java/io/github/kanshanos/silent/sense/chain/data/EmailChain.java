package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 邮箱
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class EmailChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        if (matches) {
            SenseContextHolder.addSense("email", name, data);
        }
        return matches;
    }
}
