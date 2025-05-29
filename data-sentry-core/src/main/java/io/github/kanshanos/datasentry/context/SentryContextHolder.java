package io.github.kanshanos.datasentry.context;


import lombok.experimental.UtilityClass;
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


    private static final ThreadLocal<SentryDataContext> SENTRY_DATA_CONTEXT = ThreadLocal.withInitial(SentryDataContext::new);


    public static SentryDataContext getContext() {
        return SENTRY_DATA_CONTEXT.get();
    }

    public static void setRequest(String method, String pattern) {
        getContext().setRequest(new Request(method, pattern));
    }

    public static Request getRequest() {
        return getContext().getRequest();
    }


    public static void addSensitiveData(SensitiveDataItem sensitiveData) {
        SentryDataContext context = getContext();
        List<SensitiveDataItem> sensitiveDataItems = context.getSensitiveData();
        if (sensitiveDataItems == null) {
            sensitiveDataItems = new ArrayList<>();
        }
        sensitiveDataItems.add(sensitiveData);
        context.setSensitiveData(sensitiveDataItems);
    }

    public static List<SensitiveDataItem> getSensitiveData() {
        return SENTRY_DATA_CONTEXT.get().getSensitiveData();
    }

    public static boolean shouldDetect() {
        return SENTRY_DATA_CONTEXT.get().isShouldDetect();
    }

    public static void shouldDetect(boolean shouldDetect) {
        SENTRY_DATA_CONTEXT.get().setShouldDetect(shouldDetect);
    }

    public static boolean sensitiveDataDetected() {
        return SENTRY_DATA_CONTEXT.get().isSensitiveDataDetected();
    }


    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr != null ? attr.getRequest() : null;
    }


    public static void clear() {
        SENTRY_DATA_CONTEXT.remove();
    }
}
