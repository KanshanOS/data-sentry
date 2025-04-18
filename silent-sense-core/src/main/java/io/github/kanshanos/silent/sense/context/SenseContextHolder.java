package io.github.kanshanos.silent.sense.context;


import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 组装上下文
 *
 * @author Kanshan
 * @since 2025/4/18 13:22
 */
@UtilityClass
public class SenseContextHolder {


    private static final ThreadLocal<HandlerMethod> HANDLER_METHOD_HOLDER = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = getAttributes();
        return attr != null ? attr.getRequest() : null;
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attr = getAttributes();
        return attr != null ? getAttributes().getResponse() : null;
    }

    public static ServletRequestAttributes getAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static void setHandler(HandlerMethod method) {
        HANDLER_METHOD_HOLDER.set(method);
    }

    public static HandlerMethod getHandler() {
        return HANDLER_METHOD_HOLDER.get();
    }

    public static void clear() {
        HANDLER_METHOD_HOLDER.remove();
    }
}
