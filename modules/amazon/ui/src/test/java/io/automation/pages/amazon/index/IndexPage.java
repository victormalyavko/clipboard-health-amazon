package io.automation.pages.amazon.index;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.automation.pages.amazon.SearchPage;
import io.automation.amazon.shops.Navigation;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class IndexPage implements Page, Navigation {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPage.class);

    public static SelenideElement SEARCH_BTN = $x("//input[@id='nav-search-submit-button']").as("SEARCH SUBMIT BTN");
    public static SelenideElement SEARCH_INPUT = $x("//input[@id='twotabsearchtextbox']").as("SEARCH INPUT FIELD");
    public static SelenideElement NAV_MAIN = $("#nav-main>.nav-left>#nav-hamburger-menu").as("HAMBURGER MENU");

    public IndexPage() {
        Page.assertCondition(SEARCH_BTN, Condition.interactable);
        LOGGER.info("Index page has been opened");
    }

    public <PageObjectClass> PageObjectClass openTargetElectronicCategory(String shop, String category) {
        clickWhenEnabled(NAV_MAIN);
        selectTargetElectronicCategory(shop, category);
        return Selenide.page();
    }

    public SearchPage search(String criteria) {
        setValueWhenEnabled(SEARCH_INPUT, criteria);
        clickWhenEnabled(SEARCH_BTN);
        return Page.at(SearchPage.class);
    }
}
