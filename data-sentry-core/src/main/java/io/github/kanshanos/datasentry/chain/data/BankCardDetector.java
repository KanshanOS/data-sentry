package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 银行卡号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class BankCardDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "bank_card";
    private static final int MIN_LENGTH = 12;
    private static final int MAX_LENGTH = 19;
    private static final Pattern PATTERN = Pattern.compile("^\\d{16,19}$");

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null
                || data.length() < MIN_LENGTH
                || data.length() > MAX_LENGTH
                || StringUtils.contains(data, MASK_FLAG)) return null;

        if (!PATTERN.matcher(data).matches()) return null;

        return new SensitiveDataItem(TYPE, name, data);
    }
}
