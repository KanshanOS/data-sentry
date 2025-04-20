package io.github.kanshanos.datasentry.chain.request;

import io.github.kanshanos.datasentry.properties.DataSentryProperties;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 请求 URI 白名单
 *
 * @author Kanshan
 * @since 2025/4/18 14:34
 */
public class RequestURIWhitelistFilterChain extends AbstractRequestFilterChain {

    private static final PathPatternParser parser = new PathPatternParser();

    private final List<PathPattern> excludePathPatterns;

    private final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<>();

    public RequestURIWhitelistFilterChain(DataSentryProperties properties) {
        super(properties);

        this.excludePathPatterns = properties.getExcludePathPatterns().stream()
                .map(parser::parse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean filter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        boolean excluded = cache.computeIfAbsent(uri, this::match);
        return !excluded && super.filter(request);
    }

    public boolean match(String uri) {
        PathContainer path = PathContainer.parsePath(uri);
        return this.excludePathPatterns.stream()
                .anyMatch(pathPattern -> pathPattern.matches(path));
    }
}
