package io.automation.steps;

import io.automation.pages.amazon.shops.electronics.ElectronicsPage;
import io.automation.selenide.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ElectronicsPageSteps {

    private ElectronicsPage page;

    @Then("Electronics page should be opened")
    public void electronicsPageShouldBeOpened() {
        page = Page.at(ElectronicsPage.class);
    }

    @When("I filter by brand {string} on Electronics page")
    public void filterByBrand(String brand) {
        page.filterByBrand(brand);
    }


    @And("Sort Electronics page with {string}")
    public void sortElectronicsPageWith(String option) {
        page.sortByOption(option);
    }


    @And("Select product with number {int}")
    public void selectProductWithNumber(int number) {
        page.selectProductWithNumber(number);
    }
}
