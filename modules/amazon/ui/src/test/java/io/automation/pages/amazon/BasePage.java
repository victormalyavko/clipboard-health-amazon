package io.automation.pages.amazon;

import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;

import static com.codeborne.selenide.Selenide.$x;

public class BasePage implements Page {

    public static SelenideElement SEARCH_BTN = $x("//input[@id='nav-search-submit-button']");
    public static SelenideElement SEARCH_INPUT = $x("//input[@id='twotabsearchtextbox']");

    public BasePage() {
        isClickable(SEARCH_BTN);
    }

    public ShopPage search(String criteria) {
        setValueWhenEnabled(SEARCH_INPUT, criteria);
        clickWhenEnabled(SEARCH_BTN);
        return Page.at(ShopPage.class);
    }
}
