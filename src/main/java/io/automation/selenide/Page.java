package io.automation.selenide;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.files.FileFilter;
import com.codeborne.selenide.files.FileFilters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Filter;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.not;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public interface Page {

    Logger LOGGER = LoggerFactory.getLogger(Page.class);

    SelenideElement PACE = $x("//body[contains(@class, 'pace-running')]");

    static <T> T at(Class<T> tClass) {
        return Selenide.page(tClass);
    }

    static SelenideElement assertCondition(SelenideElement element, Condition condition, Duration duration) {
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        return element.shouldBe(condition, duration);
    }

    static SelenideElement assertCondition(SelenideElement element, Condition... condition) {
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        return element.shouldBe(condition);
    }

    static ElementsCollection assertCondition(ElementsCollection elements, CollectionCondition... condition) {
        LOGGER.debug("WebElement Collection: {} should have the follow conditions: {}", elements, condition);
        return elements.shouldBe(condition);
    }

    static ElementsCollection assertCondition(ElementsCollection elements, CollectionCondition condition, Duration duration) {
        LOGGER.debug("WebElement Collection: {} should have the follow conditions: {}", elements, condition);
        return elements.shouldBe(condition, duration);
    }

    default File download(SelenideElement element) {
        return download(element, FileFilters.none());
    }

    default File download(SelenideElement element, FileFilter filter) {
        File file = null;
        try {
            file = element.download(filter);
        } catch (FileNotFoundException e) {
            LOGGER.error("No file by clicking on WebElement {} {}", element, filter.description());
        }
        return file;
    }

    default boolean anyFormatTargets(SelenideElement element) {
        String searchCriteria = element.getSearchCriteria();
        searchCriteria = searchCriteria.replaceFirst(".+\\s", "");
        return searchCriteria.contains("%s") || searchCriteria.contains("%d");
    }

    default SelenideElement format(SelenideElement element, Object... objects) {
        String searchCriteria = element.getSearchCriteria();
        String xpath = searchCriteria.substring(searchCriteria.indexOf(" "));
        return $x(String.format(xpath, objects));
    }

    default void scrollToCenterSmoothly(SelenideElement element) {
        element.scrollIntoView("{behavior: \"smooth\", block: \"center\", inline: \"center\"}");
    }

    default void scrollToWhenAvailable(SelenideElement element) {
        scrollToCenterSmoothly(element.shouldBe(exist));
    }

    default void scrollToWhenIsNotVisible(SelenideElement element) {
        if (!isElementVisible(element, Duration.ofSeconds(5))) {
            scrollToCenterSmoothly(element);
        }
    }

    default void clickWhenEnabled(SelenideElement element) {
        isClickable(element).click();
    }

    default void clickWhenEnabled(SelenideElement element, Duration time) {
        isClickable(element, time).click();
    }

    default void setValueWhenEnabled(SelenideElement element, String value) {
        isClickable(element).setValue(value);
    }

    default void clearField(SelenideElement element) {
        element.clear();
    }

    default void selectOptionFromDropdown(SelenideElement element, String option) {
        element.selectOptionContainingText(option);
    }

    default SelenideElement isClickable(SelenideElement element) {
        Condition condition = and("Clickable", appear, enabled);
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        return element.shouldBe(condition);
    }

    default SelenideElement isClickable(SelenideElement element, Duration time) {
        Condition condition = and("Clickable", appear, enabled);
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        return element.shouldBe(condition, time);
    }

    default SelenideElement isNotClickable(SelenideElement element) {
        Condition condition = not(and("Clickable", disappear, disabled));
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        return element.shouldBe(condition);
    }

    default ElementsCollection areClickable(ElementsCollection elements) {
        for (int i = 0; i < elements.size(); i++) {
            isClickable(elements.get(i));
        }
        return elements;
    }

    default ElementsCollection areNotClickable(ElementsCollection elements) {
        for (int i = 0; i < elements.size(); i++) {
            isNotClickable(elements.get(i));
        }
        return elements;
    }

    default boolean isElementMatchCondition(SelenideElement element, Condition condition) {
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        try {
            return element.shouldBe(condition).is(condition);
        } catch (com.codeborne.selenide.ex.ElementNotFound | com.codeborne.selenide.ex.ElementShould |
                 NoSuchElementException ex) {
            LOGGER.debug("WebElement: {} doesn't follow conditions: {}");
            return false;
        }
    }

    default boolean isElementMatchCondition(SelenideElement element, Condition condition, Duration timeout) {
        LOGGER.debug("WebElement: {} should have the follow conditions: {}", element, condition);
        try {
            return element.shouldBe(condition, timeout).is(condition);
        } catch (com.codeborne.selenide.ex.ElementNotFound | com.codeborne.selenide.ex.ElementShould |
                 NoSuchElementException ex) {
            LOGGER.debug("WebElement: {} doesn't follow conditions: {}");
            return false;
        }
    }

    default void selectByVisibleText(SelenideElement element, String text) {
        //TODO: works correctly only when searching for an element by Xpath(don't use CSS)
        LOGGER.debug("WebElement: {} should be visible", element);
        element.shouldBe(appear).selectOptionContainingText(text);
    }

    default void selectByIndex(SelenideElement element, int index) {
        LOGGER.debug("WebElement: {} should be visible", element);
        element.shouldBe(appear).selectOption(index);
    }

    default void selectLast(SelenideElement element) {
        // TODO Selenide options don't work as expect
        LOGGER.debug("WebElement: {} should be visible", element);
        element.shouldBe(appear);
        List<WebElement> options = new Select(element).getOptions();
        new Select(element).selectByIndex(options.size() - 1);
    }

    default void selectFirst(SelenideElement element) {
        LOGGER.debug("WebElement: {} should be visible", element);
        element.shouldBe(appear).selectOption(0);
    }

    default SelenideElement getFirstSelectedOption(SelenideElement element) {
        LOGGER.debug("WebElement: {} should be visible", element);
        element.shouldBe(appear);
        return $(new Select(element).getFirstSelectedOption());
    }

    default String getTextWhenAvailable(SelenideElement element) {
        LOGGER.debug("WebElement: {} should be visible", element);
        return element.shouldBe(appear).getText().trim();
    }

    default String getAttributeWhenAvailable(SelenideElement element, String attributeName) {
        LOGGER.debug("WebElement: {} should be visible", element);
        return element.shouldBe(Condition.attribute(attributeName)).getAttribute(attributeName);
    }

    default String getCssValueWhenAvailable(SelenideElement element, String propertyName) {
        LOGGER.debug("WebElement: {} should be visible", element);
        return element.shouldBe(appear).getCssValue(propertyName);
    }

    default SelenideElement shouldBeVisible(SelenideElement element, Duration timeout) {
        LOGGER.debug("WebElement: {} should be visible", element);
        return element.shouldBe(and("Visible", exist, visible), timeout);
    }

    default SelenideElement shouldNotBeVisible(SelenideElement element, Duration timeout) {
        LOGGER.debug("WebElement: {} should be invisible", element);
        return element.shouldNotBe(visible, timeout);
    }

    default boolean isElementVisible(SelenideElement element, Duration timeout) {
        LOGGER.debug("WebElement: {} should be visible", element);
        return isElementMatchCondition(element, visible, timeout);
    }

    default boolean isElementClickable(SelenideElement element, long durationInSeconds) {
        LOGGER.debug("WebElement: {} should be clickable", element);
        return isElementMatchCondition(element, and("Clickable", enabled, visible), Duration.ofSeconds(durationInSeconds));
    }

    default boolean isElementPresent(SelenideElement element, long durationInSeconds) {
        LOGGER.debug("WebElement: {} should be clickable", element);
        return isElementMatchCondition(element, exist, Duration.ofSeconds(durationInSeconds));
    }

    default boolean isElementPresent(By locator, Duration timeout) {
        try {
            LOGGER.debug("WebElement: By.xpath={} should be present", locator);
            new WebDriverWait(WebDriverRunner.getWebDriver(), timeout)
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (NoSuchElementException | TimeoutException e) {
            LOGGER.info(e.getMessage());
            return false;
        }
    }

    static void waitForLoadingPaceVisible() {
        try {
            PACE.should(Condition.appear);
        } catch (com.codeborne.selenide.ex.ElementNotFound | com.codeborne.selenide.ex.ElementShould |
                 NoSuchElementException ex) {
            LOGGER.warn("Pace isn't exist");
        }
    }

    static void waitForLoadingPaceInvisible(long timeOutInSeconds) {
        try {
            PACE.should(Condition.not(Condition.appear), Duration.ofSeconds(timeOutInSeconds));
        } catch (com.codeborne.selenide.ex.ElementNotFound | com.codeborne.selenide.ex.ElementShould |
                 NoSuchElementException ex) {
            LOGGER.warn("Pace is still running");
        }
    }

    static void waitForLoadingPaceInvisible() {
        try {
            PACE.should(Condition.not(Condition.appear));
        } catch (com.codeborne.selenide.ex.ElementNotFound | com.codeborne.selenide.ex.ElementShould |
                 NoSuchElementException ex) {
            LOGGER.warn("Pace is still running");
        }
    }

    default void waitForPaceIsInvisibleAndClick(SelenideElement element) {
        waitForLoadingPaceVisible();
        waitForLoadingPaceInvisible();
        clickWhenEnabled(element);
    }

    default void clickAndWaitForPaceIsInvisible(SelenideElement element) {
        clickWhenEnabled(element);
        waitForLoadingPaceVisible();
        waitForLoadingPaceInvisible();
    }
}