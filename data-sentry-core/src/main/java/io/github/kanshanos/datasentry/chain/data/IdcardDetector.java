package io.github.kanshanos.datasentry.chain.data;

import cn.hutool.core.util.IdcardUtil;
import io.github.kanshanos.datasentry.context.SensitiveDataItem;
import org.apache.commons.lang3.StringUtils;

/**
 * 身份证号码
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class IdcardDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "idcard";
    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 18;

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null
                || data.length() < MIN_LENGTH
                || data.length() > MAX_LENGTH
                || StringUtils.contains(data, MASK_FLAG)) return null;

        if (!IdcardUtil.isValidCard(data)) return null;

        return new SensitiveDataItem(TYPE, name, data);

    }
}
