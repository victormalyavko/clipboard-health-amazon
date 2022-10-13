package io.automation.pages.amazon.index;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum SortOptions {

    FEATURED("Featured"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    AVG_CUSTOMER_REVIEW("Avg. Customer Review"),
    NEWEST_ARRIVALS("Newest Arrivals");

    private static final Logger LOGGER = LoggerFactory.getLogger(SortOptions.class);

    private String value;

    SortOptions(String value) {
        this.value = value;
    }

    public static String getOption(String option) {
        for (SortOptions type : SortOptions.values()) {
            if (type.value.equalsIgnoreCase(option)) {
                LOGGER.info("{} option has been selected", type.value);
                return type.value;
            }
        }
        LOGGER.error("Wrong option: {}", option);
        throw new RuntimeException("Unexpected code issue");
    }
}
