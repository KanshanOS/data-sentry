package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * 固定电话
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class FixedPhoneDetector extends AbstractSensitiveDataDetector {

    @Override
    protected boolean detect(ContextOutput output, String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^(\\d{3,4}-?)?\\d{7,8}$");
        if (matches) {
            output.outputSensitiveItem("fixed_phone", name, data);
        }
        return matches;
    }
}
