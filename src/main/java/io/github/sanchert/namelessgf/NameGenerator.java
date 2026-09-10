package io.github.sanchert.namelessgf;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
// TODO: name reuse
public class NameGenerator {
    private static final ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public static String generateName(Class<?> clazz) {
        AutoName annotation = clazz.getAnnotation(AutoName.class);

        String prefix = getPrefix(clazz, annotation);

        if (annotation != null && !annotation.autoIncrement()) {
            return prefix;
        }

        String counterKey = clazz.getName() + ":" + prefix;
        int counter = counters.computeIfAbsent(counterKey, k -> new AtomicInteger(0))
                .incrementAndGet();

        return prefix + "_" + counter;
    }

    private static String getPrefix(Class<?> clazz, AutoName annotation) {
        if (annotation == null || annotation.prefix().isEmpty()) {
            return clazz.getSimpleName();
        }
        return annotation.prefix();
    }

    public static void resetCounter(Class<?> clazz) {
        String classPrefix = clazz.getName() + ":";
        counters.keySet().removeIf(key -> key.startsWith(classPrefix));
    }

    public static void resetCounter(Class<?> clazz, String prefix) {
        counters.remove(clazz.getName() + ":" + prefix);
    }

    public static void resetAllCounters() {
        counters.clear();
    }
}
