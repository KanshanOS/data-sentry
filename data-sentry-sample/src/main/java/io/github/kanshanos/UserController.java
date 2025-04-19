package io.github.kanshanos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kanshan
 * @since 2025/4/18 10:58
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final User user = new User()
            .setName("张三")
            .setIdcard("310112199009015335")
            .setAge(18)
            .setEmail("123456@gmail.com")
            .setAddress("上海市浦东新区")
            .setPhone("15912341234");

    @GetMapping
    public User index() {
        return user;
    }

    @GetMapping("get/{id}")
    public User get(@PathVariable("id") Long id) {
        return user;
    }
}
