package io.automation.selenide;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.and;
import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public interface Page {

    static <T> T at(Class<T> tClass) {
        return Selenide.page(tClass);
    }

    static SelenideElement assertCondition(SelenideElement element, Condition condition, Duration duration) {
        return element.shouldBe(condition, duration);
    }

    static SelenideElement assertCondition(SelenideElement element, Condition... condition) {
        return element.shouldBe(condition);
    }

    static ElementsCollection assertCondition(ElementsCollection elements, CollectionCondition... condition) {
        return elements.shouldBe(condition);
    }

    static ElementsCollection assertCondition(ElementsCollection elements, CollectionCondition condition, Duration duration) {
        return elements.shouldBe(condition, duration);
    }

    default void scrollToCenterSmoothly(SelenideElement element) {
        element.scrollIntoView("{behavior: \"smooth\", block: \"center\", inline: \"center\"}");
    }

    default void scrollToWhenAvailable(SelenideElement element) {
        scrollToCenterSmoothly(element.shouldBe(exist));
    }

    default void clickWhenEnabled(SelenideElement element) {
        assertCondition(element, Condition.interactable).click();
    }

    default void clickWhenEnabled(SelenideElement element, Duration time) {
        assertCondition(element, Condition.interactable, time).click();
    }

    default void setValueWhenEnabled(SelenideElement element, String value) {
        assertCondition(element, Condition.interactable).setValue(value);
    }

    default void clearField(SelenideElement element) {
        element.clear();
    }

    default void selectOptionFromDropdown(SelenideElement element, String option) {
        element.selectOptionContainingText(option);
    }

    default void selectByVisibleText(SelenideElement element, String text) {
        //TODO: works correctly only when searching for an element by Xpath(don't use CSS);
        element.shouldBe(appear).selectOptionContainingText(text);
    }

    default void selectByIndex(SelenideElement element, int index) {
        element.shouldBe(appear).selectOption(index);
    }

    default void selectLast(SelenideElement element) {
        // TODO Selenide options don't work as expect;
        element.shouldBe(appear);
        List<WebElement> options = new Select(element).getOptions();
        new Select(element).selectByIndex(options.size() - 1);
    }

    default void selectFirst(SelenideElement element) {
        element.shouldBe(appear).selectOption(0);
    }

    default SelenideElement getFirstSelectedOption(SelenideElement element) {
        element.shouldBe(appear);
        return $(new Select(element).getFirstSelectedOption());
    }

    default String getTextWhenAvailable(SelenideElement element) {
        return element.shouldBe(appear).getText().trim();
    }

    default String getAttributeWhenAvailable(SelenideElement element, String attributeName) {
        return element.shouldBe(Condition.attribute(attributeName)).getAttribute(attributeName);
    }

    default String getCssValueWhenAvailable(SelenideElement element, String propertyName) {
        return element.shouldBe(appear).getCssValue(propertyName);
    }

    default SelenideElement shouldBeVisible(SelenideElement element, Duration timeout) {
        return element.shouldBe(and("Visible", exist, visible), timeout);
    }

    default SelenideElement shouldNotBeVisible(SelenideElement element, Duration timeout) {
        return element.shouldNotBe(visible, timeout);
    }
}