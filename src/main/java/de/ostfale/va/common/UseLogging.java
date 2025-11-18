package de.ostfale.va.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface UseLogging {

    Map<Class<?>, Logger> LOGGER_CACHE = new ConcurrentHashMap<>();

    static Logger staticLogger() {
        Class<?> callerClass = StackWalker
                .getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .getCallerClass();
        return LOGGER_CACHE.computeIfAbsent(callerClass, LoggerFactory::getLogger);
    }

    default Logger log() {
        return LoggerFactory.getLogger(getClass());
    }

    default Logger log(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
