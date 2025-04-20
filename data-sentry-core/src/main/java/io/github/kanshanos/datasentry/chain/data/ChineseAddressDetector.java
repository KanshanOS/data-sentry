package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 中国地址
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseAddressDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "chinese_address";
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 50;
    private static final Pattern PATTERN = Pattern.compile(".*(省|市|自治区|自治州|县|区|镇|乡|村).*");

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
