package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.report.Reporter;

/**
 * 邮箱
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class EmailDetector extends AbstractSensitiveDataDetector {

    @Override
    protected boolean detect(Reporter reporter, String name, String data) {
        if (data == null) return false;
        boolean matches = data.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        if (matches) {
            reporter.report("email", name, data);
        }
        return matches;
    }
}
