package io.automation.pages;

import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;

import static com.codeborne.selenide.Selenide.$x;

public class CorePage implements Page {

    public static SelenideElement TITLE = $x("//title");
}
