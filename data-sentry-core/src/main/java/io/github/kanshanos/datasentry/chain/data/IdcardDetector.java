package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.context.SensitiveDataItem;

import java.util.regex.Pattern;

/**
 * 身份证号码
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class IdcardDetector extends AbstractSensitiveDataDetector {

    private static final String TYPE = "idcard";
    private static final int MIN_LENGTH = 15;
    private static final int MAX_LENGTH = 18;
    private static final Pattern PATTERN = Pattern.compile("(^\\d{15}$)|(^\\d{17}([0-9Xx])$)");

    @Override
    protected SensitiveDataItem detect(String name, String data) {
        if (data == null) return null;
        if (data.length() < MIN_LENGTH || data.length() > MAX_LENGTH) return null;
        // 15 位或 18 位身份证号
        boolean matches = PATTERN.matcher(data).matches();
        if (matches && data.length() == 18) {
            matches = validateIdCardChecksum(data);
        }

        return matches ? new SensitiveDataItem(TYPE, name, data) : null;

    }

    private boolean validateIdCardChecksum(String idcard) {
        int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkCodes = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.getNumericValue(idcard.charAt(i)) * weights[i];
        }
        int mod = sum % 11;
        return checkCodes[mod] == Character.toUpperCase(idcard.charAt(17));
    }
}
