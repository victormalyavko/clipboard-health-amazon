package io.automation.pages.amazon;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class CartPage extends BasePage {

    public static SelenideElement CONTENT_PRICE_LABEL = $x("//div[@class='sc-list-item-content']//span[contains(@class, 'sc-price')]");
    public static SelenideElement ACTIVE_SUBTOTALS_LABEL = $x("//form[@id='activeCartViewForm']//div[@data-name='Subtotals']//span//span[contains(@class, 'sc-price')]");
    public static SelenideElement GUTTER_SUBTOTALS_LABEL = $x("//form[@id='gutterCartViewForm']//div[@data-name='Subtotals']//span//span[contains(@class, 'sc-price')]");

    public CartPage() {
        super();
    }


}
