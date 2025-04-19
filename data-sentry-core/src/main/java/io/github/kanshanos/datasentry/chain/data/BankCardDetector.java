package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.report.Reporter;

import java.util.regex.Pattern;

/**
 * 银行卡号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class BankCardDetector extends AbstractSensitiveDataDetector {

    private static final Pattern PATTERN = Pattern.compile("^\\d{16,19}$");

    @Override
    protected boolean detect(Reporter reporter, String name, String data) {
        if (data == null) return false;
        boolean matches = PATTERN.matcher(data).matches();
        if (matches) {
            reporter.report("bank_card", name, data);
        }
        return matches;
    }
}
