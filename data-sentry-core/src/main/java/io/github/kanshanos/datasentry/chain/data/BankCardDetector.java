package io.github.kanshanos.datasentry.chain.data;

import java.util.regex.Pattern;

/**
 * 银行卡号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class BankCardDetector extends RegexBasedDetector {

    private static final DetectorConfig CONFIG = new DetectorConfig("bank_card", 12, 19);
    private static final Pattern PATTERN = Pattern.compile("^\\d{16,19}$");

    public BankCardDetector() {
        super(CONFIG, PATTERN);
    }
}
