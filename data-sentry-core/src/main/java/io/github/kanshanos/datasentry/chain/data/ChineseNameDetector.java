package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

import java.util.regex.Pattern;

/**
 * 中国姓名
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class ChineseNameDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "chinese_name";
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 5;
    private static final Pattern PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]{2,5}(·[\\u4e00-\\u9fa5]{1,4})?$");

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null) return null;
        if (data.length() < MIN_LENGTH || data.length() > MAX_LENGTH) return null;
        boolean matches = PATTERN.matcher(data).matches();
        return matches ? new SensitiveDataItem(TYPE, name, data) : null;
    }
}
