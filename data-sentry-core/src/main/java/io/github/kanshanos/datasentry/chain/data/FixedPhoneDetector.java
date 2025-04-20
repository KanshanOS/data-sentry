package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.util.PhoneUtil;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 固定电话
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class FixedPhoneDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "fixed_phone";
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 15;

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null
                || data.length() < MIN_LENGTH
                || data.length() > MAX_LENGTH
                || StringUtils.contains(data, MASK_FLAG)) return null;

        if (!PhoneUtil.isTel(data)) return null;

        return new SensitiveDataItem(TYPE, name, data);
    }
}
