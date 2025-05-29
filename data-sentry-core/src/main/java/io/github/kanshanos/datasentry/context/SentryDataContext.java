package io.github.kanshanos.datasentry.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 敏感数据检测上下文信息
 *
 * @author Kanshan
 * @since 2025/4/18 15:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentryDataContext {

    /**
     * 请求信息
     */
    private Request request;

    /**
     * 敏感数据信息
     */
    private List<SensitiveDataItem> sensitiveData;

    /**
     * 当前请求是否需要被检测
     */
    private boolean shouldDetect = false;

    /**
     * 是否检测到敏感数据
     */
    private boolean sensitiveDataDetected = false;


    public void setSensitiveData(List<SensitiveDataItem> sensitiveData) {
        this.sensitiveData = sensitiveData;
        this.sensitiveDataDetected = sensitiveData != null && !sensitiveData.isEmpty();
    }
}
