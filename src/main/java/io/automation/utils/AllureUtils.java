package io.automation.utils;

import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AllureUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);

    private AllureUtils() {
    }

    private static Optional<byte[]> getScreenshotBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted()
                    ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES))
                    : Optional.empty();
        } catch (WebDriverException e) {
            LOGGER.warn("Could not get screen shot", e);
            return Optional.empty();
        }
    }

    public static void attachScreenshot() {
        attachScreenshot("Screenshot");
    }

    public static void attachScreenshot(String name) {
        getScreenshotBytes()
                .ifPresent(bytes -> Allure.getLifecycle()
                        .addAttachment(name, "image/png", "png", bytes));
    }
}
