package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.lang.Validator;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 手机号
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class MobileDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "mobile";
    private static final int MIN_LENGTH = 11;
    private static final int MAX_LENGTH = 14;


    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null
                || data.length() < MIN_LENGTH
                || data.length() > MAX_LENGTH
                || StringUtils.contains(data, MASK_FLAG)) return null;

        if (!Validator.isMobile(data)) return null;

        return new SensitiveDataItem(TYPE, name, data);
    }
}
