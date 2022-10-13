package io.automation.pages.amazon;

import com.codeborne.selenide.ElementsCollection;
import io.automation.pages.amazon.index.IndexPage;

import static com.codeborne.selenide.Selenide.$$x;

public class SearchPage extends IndexPage {

    public static ElementsCollection RESULTS = $$x("//div[contains(@cel_widget_id,'MAIN-SEARCH_RESULTS')]");

    public SearchPage() {
        super();
    }

}
