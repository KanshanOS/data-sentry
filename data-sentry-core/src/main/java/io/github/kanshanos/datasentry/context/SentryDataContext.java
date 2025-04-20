package io.github.kanshanos.datasentry.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 敏感数据信息
 *
 * @author Kanshan
 * @since 2025/4/18 15:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentryDataContext {

    private RequestHandler request;

    private List<SensitiveDataItem> sensitiveData;

    public boolean hit() {
        return sensitiveData != null && !sensitiveData.isEmpty();
    }
}
