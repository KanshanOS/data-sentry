package io.github.kanshanos.datasentry.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;


/**
 * 配置属性
 *
 * @author Kanshan
 * @since 2025/4/18 10:51
 */
@Data
@ConfigurationProperties(prefix = DataSentryProperties.PREFIX)
public class DataSentryProperties {
    public static final String PREFIX = "kanshanos.datasentry";

    /**
     * 是否启用 DataSentry
     */
    private boolean enabled = true;

    /**
     * 采样率，范围 [0, 1]，决定请求被处理的概率
     */
    private double samplingRate = 0.5;

    /**
     * 排除路径
     */
    private List<String> excludePathPatterns = Collections.emptyList();

    /**
     * 请求频率限制的时间间隔（秒），在此期间阻止重复处理，默认：10分钟
     */
    private long requestRateWindowIntervalSeconds = 10 * 60;

    /**
     * 敏感数据检测缓存的有效期（秒），在此期间跳过重复检测，默认：7天
     */
    private long sensitiveDetectionHitWindowIntervalSeconds = 24 * 60 * 60;

}
