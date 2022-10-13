package io.automation.selenide;

import com.codeborne.selenide.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConditionSuite {

    APPEAR("appear", Condition.appear),
    APPEARS("appears", Condition.appear),
    DISAPPEAR("disappear", Condition.disappear),
    DISAPPEARS("disappears", Condition.disappear),
    CLICKABLE("clickable", Condition.interactable),
    INTERACTABLE("interactable", Condition.interactable),
    EXIST("exist", Condition.exist),
    HIDDEN("hidden", Condition.hidden);

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionSuite.class);

    private String value;
    private Condition condition;

    ConditionSuite(String value, Condition condition) {
        this.value = value;
        this.condition = condition;
    }

    public static Condition getCondition(String condition) {
        for (ConditionSuite type : ConditionSuite.values()) {
            if (type.value.equalsIgnoreCase(condition)) {
                return type.condition;
            }
        }
        LOGGER.error("Wrong condition: {}", condition);
        throw new RuntimeException("Unexpected code issue");
    }
}
