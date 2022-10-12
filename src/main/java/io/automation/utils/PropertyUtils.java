package io.automation.utils;

import java.io.FileInputStream;
import java.util.Properties;

import static io.qameta.allure.Allure.addAttachment;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;

public final class PropertyUtils {

    private static final String PROPERTY_PATH = "gradle.properties";

    private PropertyUtils() {
    }

    public static String getProperty(String name) {
        try (FileInputStream fis = new FileInputStream(PROPERTY_PATH)) {
            Properties property = new Properties();
            property.load(fis);
            return property.getProperty(name);
        } catch (Exception error) {
            addAttachment(format("Error to property by name %s", name), error.getMessage());
        }
        return null;
    }

    public static int getIntProperty(String name) {
        return parseInt(getProperty(name));
    }

    public static long getLongProperty(String name) {
        return parseLong(getProperty(name));
    }
}
