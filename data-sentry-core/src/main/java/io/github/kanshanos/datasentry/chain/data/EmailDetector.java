package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.lang.Validator;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 邮箱
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class EmailDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "email";
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 30;

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null
                || data.length() < MIN_LENGTH
                || data.length() > MAX_LENGTH
                || StringUtils.contains(data, MASK_FLAG)) return null;

        if (!Validator.isEmail(data)) return null;

        return new SensitiveDataItem(TYPE, name, data);
    }
}
