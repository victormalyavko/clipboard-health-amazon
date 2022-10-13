package io.automation.pages;

import com.codeborne.selenide.As;
import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;

import static com.codeborne.selenide.Selenide.$x;

public class CorePage implements Page {

    @As("TITLE")
    public static SelenideElement TITLE = $x("//title");
}
