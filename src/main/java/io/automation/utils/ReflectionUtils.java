package io.automation.utils;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static boolean doesObjectOfType(Object object, String type) {
        return object.getClass().getSimpleName().equals(type);
    }
}