package io.automation.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.automation.pages.CorePage;
import io.automation.selenide.Browser;
import io.automation.selenide.ConditionSuite;
import io.automation.selenide.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static com.codeborne.selenide.Selenide.$x;

public class PageSteps {
    private static final CorePage PAGE = new CorePage();

    @Given("^Open page \"([^\"]*)\"$")
    public void openPage(String url) {
        Browser.open(url);
    }

    @Given("^Go to \"([^\"]*)\"$")
    public void goToUrl(String url) {
        Browser.open(url);
    }

    @Then("^Page should be opened with WebElement \"([^\"]*)\"$")
    public void pageShouldBeOpenedWithWebElement(String xpath) {
        SelenideElement element = $x(xpath);
        Page.assertCondition(element, Condition.appear);
    }

    @Then("^Page with WebElement \"([^\"]*)\" should be \"([^\"]*)\"$")
    public void pageWithWebElementShouldBe(String xpath, String condition) {
        SelenideElement element = $x(xpath);
        Condition expected = ConditionSuite.getCondition(condition);
        Page.assertCondition(element, expected);
    }

    @Then("^Page should has \"([^\"]*)\" WebElement \"([^\"]*)\" AS \"([^\"]*)\"$")
    public void pageWithWebElementAsAliasShouldBe(String condition, String xpath, String alias) {
        SelenideElement element = $x(xpath).as(alias);
        Condition expected = ConditionSuite.getCondition(condition);
        Page.assertCondition(element, expected);
    }

    @When("^I click WebElement \"([^\"]*)\"$")
    public void clickWebElement(String xpath) {
        SelenideElement element = $x(xpath);
        PAGE.clickWhenEnabled(element);
    }

    @When("^I click WebElement \"([^\"]*)\" AS \"([^\"]*)\"$")
    public void clickWebElement(String xpath, String alias) {
        SelenideElement element = $x(xpath).as(alias);
        if (element.isDisplayed()) {
            PAGE.clickWhenEnabled(element);
        }
    }

    @When("^I input \"([^\"]*)\" in field \"([^\"]*)\"$")
    public void inputValueInField(String value, String xpath) {
        SelenideElement element = $x(xpath);
        PAGE.setValueWhenEnabled(element, value);
    }

    @Then("Page should be opened with title {string}")
    public void pageShouldBeOpenedWithTitle(String title) {
        Page.assertCondition(CorePage.TITLE, Condition.and(
                "Title text",
                Condition.attribute("innerText", title),
                Condition.attribute("localName", "title")));
    }

}
