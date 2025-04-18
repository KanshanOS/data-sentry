package io.github.kanshanos.silent.sense.chain.data;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;

/**
 * 银行卡号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class BankCardChain extends AbstractSenseDataChain {

    @Override
    protected boolean match(String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^\\d{16,19}$");
        if (matches) {
            SenseContextHolder.addSense("bank_card", name, data);
        }
        return matches;
    }
}
