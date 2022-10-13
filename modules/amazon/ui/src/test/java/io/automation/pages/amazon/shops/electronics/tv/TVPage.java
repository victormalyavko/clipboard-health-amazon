package io.automation.pages.amazon.shops.electronics.tv;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;

public class TVPage implements Page {

    private static final Logger LOGGER = LoggerFactory.getLogger(TVPage.class);
    public static SelenideElement PRODUCT_TITLE = $("#productTitle").as("Product title");
    public static SelenideElement DESCRIPTION_TITLE = $("#feature-bullets>h1").as("Description title");

    public TVPage() {
        Page.assertCondition(PRODUCT_TITLE, Condition.appear);
        LOGGER.info("TV page has been opened");
    }
}
