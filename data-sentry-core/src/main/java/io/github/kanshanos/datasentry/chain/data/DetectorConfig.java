package io.github.kanshanos.datasentry.chain.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 检测器配置
 *
 * @author Kanshan
 * @since 2025/4/20
 */
@Data
@AllArgsConstructor
public class DetectorConfig {
    private String type;
    private int minLength;
    private int maxLength;
}
