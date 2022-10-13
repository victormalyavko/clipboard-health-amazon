package io.automation.pages.amazon.shops.electronics;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Browser;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$x;

public class ElectronicsPage implements Page {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectronicsPage.class);

    public static SelenideElement ELECTRONICS_IMG = $x("//span[@class='nav-a-content']/img[@alt='Electronics']");
    public static SelenideElement DROPDOWN_FEATURED = $x("//form[@action='/s']/span[contains(@class,'dropdown')]/select");

    public ElectronicsPage() {
        Page.assertCondition(ELECTRONICS_IMG, Condition.appear);
        LOGGER.info("Electronics page has been opened");
    }

    public ElectronicsPage filterByBrand(String brand) {
        final String xpath = String.format("//span[normalize-space(.)='Brands']/../following-sibling::ul//span[normalize-space(.)='%s']//div//i[contains(@class,'checkbox')]", brand);
        SelenideElement checkbox = $x(xpath);
        clickWhenEnabled(checkbox);
        return Page.at(ElectronicsPage.class);
    }

    public ElectronicsPage sortByOption(String option) {
        selectByVisibleText(DROPDOWN_FEATURED, option);
        return Page.at(ElectronicsPage.class);
    }

    public ElectronicsPage selectProductWithNumber(int number) {
        final String xpath = String.format("//span[@data-component-type='s-search-results']//div[@cel_widget_id='MAIN-SEARCH_RESULTS-%d']//div[contains(@class,'s-title')]//a", number);
        SelenideElement product = $x(xpath);
        clickWhenEnabled(product);
        Browser.switchToWindow(1);
        return Page.at(ElectronicsPage.class);
    }
}
