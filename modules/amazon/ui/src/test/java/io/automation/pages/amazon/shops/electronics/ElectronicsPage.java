package io.automation.pages.amazon.shops.electronics;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.automation.pages.amazon.shops.electronics.tv.TVPage;
import io.automation.selenide.Browser;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ElectronicsPage implements Page {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectronicsPage.class);

    public static SelenideElement ELECTRONICS_IMG = $x("//span[@class='nav-a-content']/img[@alt='Electronics']").as("Electronics IMG");

    public static SelenideElement DROPDOWN_FEATURED = $x("//form[@action='/s']/span[contains(@class,'dropdown')]/select").as("FEATURED DROPDOWN");
    public static SelenideElement FILTER = $("#s-refinements").as("FILTER");

    public ElectronicsPage() {
        Page.assertCondition(ELECTRONICS_IMG, Condition.appear);
        Page.assertCondition(FILTER, Condition.appear);
        LOGGER.info("Electronics page has been opened");
    }

    public ElectronicsPage filterByBrand(String brand) {
        final String xpath = String.format("//span[normalize-space(.)='Brands']/../following-sibling::ul//span[normalize-space(.)='%s']//div//i[contains(@class,'checkbox')]", brand);
        SelenideElement checkbox = $x(xpath).as("CHECKBOX for " + brand).scrollIntoView(true);
        clickWhenEnabled(checkbox);
        return Page.at(ElectronicsPage.class);
    }

    public ElectronicsPage sortByOption(String option) {
        selectByVisibleText(DROPDOWN_FEATURED, option);
        return Page.at(ElectronicsPage.class);
    }

    public TVPage selectProductWithNumber(int number) {
        final String xpath = String.format("//span[@data-component-type='s-search-results']//div[@cel_widget_id='MAIN-SEARCH_RESULTS-%d']//div[contains(@class,'s-title')]//a", number);
        final String alias = String.format("title LINK to product with number [%d]", number);
        SelenideElement product = $x(xpath).as(alias);
        clickWhenEnabled(product);
        Browser.switchToWindow(1);
        return Page.at(TVPage.class);
    }
}
