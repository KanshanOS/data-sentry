package io.github.kanshanos.silent.sense.decider.request;

import io.github.kanshanos.silent.sense.properties.SilentSenseProperties;
import org.springframework.http.server.PathContainer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求 URI 白名单
 *
 * @author Kanshan
 * @since 2025/4/18 14:34
 */
public class RequestURIWhitelistDecider extends AbstractSenseRequestDecider {

    private static final PathPatternParser parser = new PathPatternParser();

    private final List<PathPattern> excludePathPatterns;

    public RequestURIWhitelistDecider(SilentSenseProperties properties) {
        super(properties);

        this.excludePathPatterns = properties.getExcludePathPatterns().stream()
                .map(parser::parse)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean check(HttpServletRequest request, HandlerMethod handler) {
        return !match(request.getRequestURI());
    }

    public boolean match(String uri) {
        PathContainer path = PathContainer.parsePath(uri);
        return this.excludePathPatterns.stream()
                .anyMatch(pathPattern -> pathPattern.matches(path));
    }
}
