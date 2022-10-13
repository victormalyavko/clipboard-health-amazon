package io.automation.pages.amazon.shops.electronics.tv;

import com.codeborne.selenide.SelenideElement;
import io.automation.pages.amazon.IndexPage;

import static com.codeborne.selenide.Selenide.$;

public class TVPage extends IndexPage {

    public static SelenideElement DESCRIPTION_TITLE = $("#feature-bullets>h1");

    public TVPage() {
        super();
    }
}
