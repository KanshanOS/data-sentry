package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * 手机号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class MobileDetector extends AbstractSensitiveDataDetector {

    @Override
    protected boolean detect(ContextOutput output, String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^1[3-9]\\d{9}$");
        if (matches) {
            output.outputSensitiveItem("mobile", name, data);
        }
        return matches;
    }
}
