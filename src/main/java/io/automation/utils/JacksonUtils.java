package io.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JacksonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    private JacksonUtils() {
    }

    public static String asJsonString(Object object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed convert POJO to JSON String");
            throw new RuntimeException(e);
        }
    }
}
