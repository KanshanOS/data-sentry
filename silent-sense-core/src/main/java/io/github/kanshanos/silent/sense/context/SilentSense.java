package io.github.kanshanos.silent.sense.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.method.HandlerMethod;

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
public class SilentSense {
    private HandlerMethod handler;

    private List<SenseItem> senses;
}
