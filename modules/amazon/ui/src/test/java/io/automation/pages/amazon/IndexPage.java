package io.automation.pages.amazon;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class IndexPage implements Page {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexPage.class);

    public static SelenideElement SEARCH_BTN = $x("//input[@id='nav-search-submit-button']");
    public static SelenideElement SEARCH_INPUT = $x("//input[@id='twotabsearchtextbox']");
    public static SelenideElement NAV_MAIN = $("#nav-main>.nav-left>#nav-hamburger-menu");

    public IndexPage() {
        Page.assertCondition(SEARCH_BTN, Condition.interactable);
        LOGGER.info("Index page has been opened");
    }

    public <PageObjectClass> PageObjectClass openTargetCategory(String category, String target) {
        SelenideElement category_shop = $x(String.format("//li/a/div[normalize-space(.)='%s']/parent::a", category));
//        SelenideElement category_shop_title = $x("//div[normalize-space(.)='shop by category']");
        SelenideElement category_target = $x(String.format("//a[normalize-space(.)='%s']", target));
        clickWhenEnabled(NAV_MAIN);
//        Page.assertCondition(category_shop_title, Condition.appear);
        clickWhenEnabled(category_shop);
        clickWhenEnabled(category_target);
        return Selenide.page();
    }

    public SearchPage search(String criteria) {
        setValueWhenEnabled(SEARCH_INPUT, criteria);
        clickWhenEnabled(SEARCH_BTN);
        return Page.at(SearchPage.class);
    }
}
