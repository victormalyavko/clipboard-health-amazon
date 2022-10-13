package io.automation.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.automation.pages.amazon.shops.electronics.tv.TVPage;
import io.automation.selenide.ConditionSuite;
import io.automation.selenide.Page;
import io.cucumber.java.en.Then;

public class TVPageSteps {

    @Then("{string} section should be {string} on the Television Page")
    public void sectionShouldBe(String alias, String condition) {
        SelenideElement element = TVPage.DESCRIPTION_TITLE.as(alias);
        Condition expected = ConditionSuite.getCondition(condition);
        Page.assertCondition(element, Condition.and(
                "Description section",
                expected, Condition.text(alias))
        );
    }
}
