package io.github.kanshanos.datasentry.context;


import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 组装上下文
 *
 * @author Kanshan
 * @since 2025/4/18 13:22
 */
@UtilityClass
public class SentryContextHolder {


    private static final ThreadLocal<RequestHandler> REQUEST_HANDLER = new ThreadLocal<>();
    private static final ThreadLocal<List<SensitiveDataItem>> SENSITIVE_DATA = new ThreadLocal<>();

    public static void addSensitiveData(String type, String name, String data) {
        addSensitiveData(new SensitiveDataItem(type, name, data));
    }

    public static void addSensitiveData(SensitiveDataItem sensitiveData) {
        List<SensitiveDataItem> sensitiveDataItems = SENSITIVE_DATA.get();
        if (sensitiveDataItems == null) {
            sensitiveDataItems = new ArrayList<>();
        }
        sensitiveDataItems.add(sensitiveData);
        SENSITIVE_DATA.set(sensitiveDataItems);
    }

    public static List<SensitiveDataItem> getSensitiveData() {
        return SENSITIVE_DATA.get();
    }

    public static boolean hit() {
        return !CollectionUtils.isEmpty(SENSITIVE_DATA.get());
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr != null ? attr.getRequest() : null;
    }


    public static void setRequestHandler(String method, String pattern) {
        REQUEST_HANDLER.set(new RequestHandler(method, pattern));
    }


    public static RequestHandler getRequestHandler() {
        return REQUEST_HANDLER.get();
    }

    public static void clear() {
        REQUEST_HANDLER.remove();
        SENSITIVE_DATA.remove();
    }
}
