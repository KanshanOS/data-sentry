package io.github.kanshanos.silent.sense.output;

import io.github.kanshanos.silent.sense.context.SenseContextHolder;
import io.github.kanshanos.silent.sense.context.SenseItem;
import io.github.kanshanos.silent.sense.context.SilentSense;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * Slf4jOutput
 *
 * @author Kanshan
 * @since 2025/4/18 17:05
 */
@Slf4j
public class Slf4jOutput implements Output {
    @Override
    public void output(SilentSense sense) {
        log.info("Sense URI : {}, Senses List : {}", SenseContextHolder.bestMatchingPattern(),
                sense.getSenses().stream().map(SenseItem::format).collect(Collectors.joining(",")));
    }
}
