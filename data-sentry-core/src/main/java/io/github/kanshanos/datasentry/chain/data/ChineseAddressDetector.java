package io.github.kanshanos.datasentry.chain.data;

import java.util.regex.Pattern;

/**
 * 中国地址
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseAddressDetector extends RegexBasedDetector {

    private static final DetectorConfig CONFIG = new DetectorConfig("chinese_address", 10, 50);
    private static final Pattern PATTERN = Pattern.compile(".*(省|市|自治区|自治州|县|区|镇|乡|村).*");

    public ChineseAddressDetector() {
        super(CONFIG, PATTERN);
    }
}
