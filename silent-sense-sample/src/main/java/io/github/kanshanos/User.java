package io.github.kanshanos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 测试用户对象类
 *
 * @author Kanshan
 * @since 2025/4/18 10:57
 */
@Data
@Accessors(chain = true)
public class User {
    private String name;
    private String idcard;
    private Integer age;
    private String email;
    private String phone;
    private String address;
}
