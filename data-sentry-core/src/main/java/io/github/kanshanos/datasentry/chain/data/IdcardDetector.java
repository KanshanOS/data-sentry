package io.github.kanshanos.datasentry.chain.data;

import io.github.kanshanos.datasentry.output.ContextOutput;

/**
 * 身份证号码
 *
 * @author Kanshan
 * @since 2025/4/18 15:20
 */
public class IdcardDetector extends AbstractSensitiveDataDetector {

    @Override
    protected boolean detect(ContextOutput output, String name, String data) {
        if (data == null) return false;
        // 15 位或 18 位身份证号
        boolean matches = data.matches("(^\\d{15}$)|(^\\d{17}([0-9Xx])$)");
        if (matches && data.length() == 18) {
            matches = validateIdCardChecksum(data);
        }
        if (matches) {
            output.outputSensitiveItem("idcard", name, data);
        }
        return matches;
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
