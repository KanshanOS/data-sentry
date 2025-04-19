package io.github.kanshanos.datasentry.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 敏感数据信息
 *
 * @author Kanshan
 * @since 2025/4/18 15:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveDataItem {
    private String type;
    private String name;
    private String data;


    public String format() {
        return String.format("{\"type\":\"%s\",\"name\":\"%s\",\"data\":\"%s\"}", type, name, data);
    }
}
