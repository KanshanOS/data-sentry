package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

import java.util.regex.Pattern;

/**
 * 邮箱
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class EmailDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "email";
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 20;
    private static final Pattern PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null) return null;
        if (data.length() < MIN_LENGTH || data.length() > MAX_LENGTH) return null;
        boolean matches = PATTERN.matcher(data).matches();
        return matches ? new SensitiveDataItem(TYPE, name, data) : null;
    }
}
