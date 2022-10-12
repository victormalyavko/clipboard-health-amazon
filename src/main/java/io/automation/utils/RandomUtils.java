package io.automation.utils;

import static java.lang.String.format;
import static org.apache.commons.lang3.RandomStringUtils.random;

public final class RandomUtils {

    private static final String PREFIX_CLIENT_REF_ID = "prefixAutomation-";
    private static final String CHARS = "1234567890";
    private static final int RANGE = 10;

    private RandomUtils() {
    }

    /**
     * Generates a random ClientReferenceId.
     *
     * @return a random ClientReferenceId.
     */
    public static String generateClientRefId() {
        return format("%s%s", PREFIX_CLIENT_REF_ID, random(RANGE, CHARS));
    }

    /**
     * Generates a random ClientReferenceId.
     *
     * @return a random ClientReferenceId.
     */
    public static String generateClientRefId(int range) {
        return format("%s%s", PREFIX_CLIENT_REF_ID, random(range - PREFIX_CLIENT_REF_ID.length(), CHARS));
    }
}