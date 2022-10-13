package io.automation.steps;

import io.automation.pages.amazon.index.IndexPage;
import io.automation.selenide.Page;
import io.cucumber.java.en.When;

public class StartPageSteps {

    private IndexPage page;

    @When("I select the category {string} and click shop {string}")
    public void selectCategoryAndShop(String category, String shop) {
        this.page = Page.at(IndexPage.class);
        page.openTargetElectronicCategory(category, shop);
    }
}
