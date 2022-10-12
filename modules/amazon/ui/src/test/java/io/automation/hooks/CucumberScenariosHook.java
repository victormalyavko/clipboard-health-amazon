package io.automation.hooks;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.junit.AssumptionViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CucumberScenariosHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(CucumberScenariosHook.class);

    @Before(order = 0)
    public void before(final Scenario scenario) {
        final ArrayList<String> scenarioTags = new ArrayList<>(scenario.getSourceTagNames());

        if (checkForSkipScenario(scenarioTags)) {
            LOGGER.warn("This scenario: \"{}\" marked as @skipped", scenario.getName());
            throw new AssumptionViolatedException(String.format("This scenario: \"%s\" has been marked as @skipped", scenario.getName()));
        } else {
            LOGGER.info("This scenario: \"{}\" hasn't been marked as @skipped", scenario.getName());
        }
    }

    private boolean checkForSkipScenario(final ArrayList<String> scenarioTags) {
        // I use a tag "@Feature-01AXX" on the scenarios which needs to be run when the feature is enabled on the appliance/application
        return scenarioTags.stream()
                .anyMatch(tag -> tag.contains("skipped"));
    }
}
