package io.automation.selenide;

import com.codeborne.selenide.DownloadsFolder;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.switchTo;

public interface Browser {

    static WebDriver switchToWindow(int index) {
        return switchTo().window(index);
    }

    static WebDriver switchToFrame(int index) {
        return switchTo().frame(index);
    }

    static WebDriver switchToFrame(String idOrName) {
        return switchTo().frame(idOrName);
    }

    static WebDriver switchToFrame(WebElement frameElement) {
        return switchTo().frame(frameElement);
    }

    static WebDriver switchToDefault() {
        return switchTo().defaultContent();
    }

    static Alert switchToAlert() {
        return switchTo().alert();
    }

    static void refresh() {
        WebDriverRunner.getAndCheckWebDriver().navigate().refresh();
    }

    static void toPreviousPage() {
        WebDriverRunner.getAndCheckWebDriver().navigate().back();
    }

    static void open(String url) {
        Selenide.open(url);
    }

    static void closeBrowser() {
        Selenide.closeWebDriver();
    }

    static String getCurrentUrl() {
        return WebDriverRunner.getAndCheckWebDriver().getCurrentUrl();
    }

    static String getPath() {
        return getCurrentUrl().replaceFirst(".+(?:[.]com)", "");
    }

    static String getBaseUrl() {
        return getCurrentUrl().replaceFirst(String.format("(?:%s).*", getPath()), "");
    }

    static void maximize() {
        WebDriverRunner.getWebDriver().manage().window().maximize();
    }

    static DownloadsFolder getDownloadFolder() {
        return WebDriverRunner.getBrowserDownloadsFolder();
    }

}
