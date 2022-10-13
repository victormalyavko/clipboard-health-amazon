package io.automation.pages.amazon.shops;

import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$x;

public enum ShopMenu {

    TV_APPLIANCES_ELECTRONICS("TV, Appliances, Electronics", $x("//li/a/div[normalize-space(.)='TV, Appliances, Electronics']/parent::a")),
    MOBILES_COMPUTERS("Mobiles, Computers", $x("//li/a/div[normalize-space(.)='Mobiles, Computers']/parent::a"));

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopMenu.class);

    private String value;
    private SelenideElement element;

    ShopMenu(String value, SelenideElement element) {
        this.value = value;
        this.element = element.as(value);
    }

    public static SelenideElement getShop(String category) {
        for (ShopMenu type : ShopMenu.values()) {
            if (type.value.equalsIgnoreCase(category)) {
                LOGGER.info("{} shop has been selected", type.value);
                return type.element;
            }
        }
        LOGGER.error("Wrong shop: {}", category);
        throw new RuntimeException("Unexpected code issue");
    }
}
