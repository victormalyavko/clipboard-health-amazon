package io.automation.hooks;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.automation.selenide.Browser;
import io.automation.selenide.Durations;
import io.automation.utils.FileUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class SelenideHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelenideHook.class);
    private static final String BROWSER_SIZE = System.getProperty("browser.resolution", "1920x1080");
    private static final String BROWSER_NAME = System.getProperty("browser", "chrome");

    private static final String SELENOID_REMOTE = System.getProperty("selenoid.remote", "http://127.0.0.1:4444/wd/hub");
    private static final boolean SELENOID_VIDEO = Boolean.parseBoolean(System.getProperty("selenoid.video", "false"));
    private static final boolean SELENOID = Boolean.parseBoolean(System.getProperty("selenoid.enable", "false"));

    private Optional<byte[]> getVideoBytes(String videoName) {
        try {
            return Optional.of(Files.readAllBytes(Paths.get("selenoid/config/video/" + videoName)));
        } catch (IOException e) {
            LOGGER.warn("Could not get video", e);
            return Optional.empty();
        }
    }

    private Optional<byte[]> getScreenshotBytes() {
        try {
            return WebDriverRunner.hasWebDriverStarted()
                    ? Optional.of(((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES))
                    : Optional.empty();
        } catch (WebDriverException e) {
            LOGGER.warn("Could not get screen shot", e);
            return Optional.empty();
        }
    }

    private void attachVideo(Scenario scenario) {
        final String videoName = formatVideoName(scenario);
        if (scenario.getStatus() != Status.PASSED && SELENOID_VIDEO) {
            {
                try {
                    StopWatch timer = new StopWatch();
                    timer.start();
                    while (timer.getTime() < TimeUnit.MILLISECONDS.convert(Duration.ofSeconds(15))) {
                        if (Files.exists(Paths.get("selenoid/config/video/" + videoName))) {
                            LOGGER.info("\"selenoid/config/video/{}\" is exist", videoName);
                            break;
                        }
                        Thread.sleep(TimeUnit.MILLISECONDS.convert(Duration.ofMillis(300)));
                    }
                    timer.stop();
                } catch (InterruptedException e) {
                    LOGGER.warn("\"selenoid/config/video/{}\" is miss", videoName);
                    Thread.currentThread().interrupt();
                }
                getVideoBytes(videoName)
                        .ifPresent(bytes -> Allure.getLifecycle()
                                .addAttachment("Video", "video/mp4", "mp4", bytes));
            }

        }
    }

    private String formatVideoName(Scenario scenario) {
        return scenario.getName()
                .replaceAll("\\W", " ").trim()
                .replaceAll("\\s{2,}", " ").replaceAll("\\s", "_")
                .concat(".mp4");
    }

    private void printBrowserSettings() {
        Dimension windowSize = WebDriverRunner.getWebDriver().manage().window().getSize();
        Capabilities capabilities = ((HasCapabilities) WebDriverRunner.getWebDriver()).getCapabilities();
        LOGGER.info("Browser window size: {}", windowSize);
        LOGGER.info("Capabilities: {}", capabilities);
    }

    private DesiredCapabilities setChromeCapabilities() {
        LOGGER.info("Set capabilities for ChromeDriver");
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        logPrefs.enable(LogType.PROFILER, Level.ALL);
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        logPrefs.enable(LogType.DRIVER, Level.ALL);
        logPrefs.enable(LogType.CLIENT, Level.ALL);
        logPrefs.enable(LogType.SERVER, Level.ALL);

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-dev-shm-usage");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableLog", true);
        capabilities.setCapability(ChromeOptions.LOGGING_PREFS, logPrefs);
        capabilities.setCapability("sessionTimeout", "3m");
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        return capabilities;
    }

    private DesiredCapabilities setFirefoxCapabilities() {
        LOGGER.info("Set capabilities for Firefox");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setPreference("browser.download.folderList", 2);
        firefoxProfile.setPreference("intl.accept_languages", "en,en-US");
        firefoxProfile.setPreference("browser.download.useDownloadDir", true);
        firefoxProfile.setPreference("browser.download.viewableInternally.enabledTypes", "");
        firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf;text/plain;application/text;text/xml;application/xml");
        firefoxProfile.setPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
        firefoxOptions.setProfile(firefoxProfile);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        return capabilities;
    }

    private DesiredCapabilities setIECapabilities() {
        LOGGER.info("Set capabilities for Internet Explorer");
        InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("se:ieOptions", ieOptions);
        return capabilities;
    }

    private DesiredCapabilities setSafariCapabilies() {
        LOGGER.info("Set capabilities for Safari");
        SafariOptions options = new SafariOptions();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("safari.options", options);
        return capabilities;
    }

    private DesiredCapabilities defaultOrAny() {
        switch (BROWSER_NAME) {
            case "chrome":
                return setChromeCapabilities();
            case "firefox":
                return setFirefoxCapabilities();
            case "internet explorer":
                return setIECapabilities();
            case "safari":
                return setSafariCapabilies();
            default:
                LOGGER.error("Invalid browser name: {}", BROWSER_NAME);
                throw new RuntimeException("Invalid browser name: " + BROWSER_NAME);
        }
    }

    /**
     * Method detects Apple Silicon (arm64) arch
     *
     * @return arm64 or nothing
     */
    private String setArmOrDefault() {
        String version = StringUtils.EMPTY;
        try {
            version = new String(Runtime.getRuntime().exec("sysctl -in sysctl.proc_translated").getInputStream().readAllBytes()).trim();
        } catch (IOException e) {
            LOGGER.error("Command line execution issue: {}", e.getMessage());
            e.printStackTrace();
        }
        return version.equals("1") ? "arm64" : null;
    }

    @Before(order = 1)
    public void prepareSelenoidConfig(final Scenario scenario) {
        DesiredCapabilities capabilities = defaultOrAny();
        Map<String, Object> selenoidOptions = Map.of(
                "videoName", formatVideoName(scenario),
                "enableVideo", SELENOID_VIDEO,
                "sessionTimeout", "3m",
                "enableVNC", true,
                "enableLog", true
        );
        capabilities.setCapability("selenoid:options", selenoidOptions);
        Configuration.driverManagerEnabled = !SELENOID;
        Configuration.remote = SELENOID ? SELENOID_REMOTE : null;
        Configuration.headless = false;
        Configuration.browser = BROWSER_NAME;
        Configuration.browserSize = BROWSER_SIZE;
        Configuration.browserVersion = setArmOrDefault();
        Configuration.browserCapabilities = capabilities;
        Configuration.pageLoadTimeout = TimeUnit.MILLISECONDS.convert(Durations.QUARTER_TO_MIN, TimeUnit.SECONDS);
        Configuration.timeout = TimeUnit.MILLISECONDS.convert(Durations.QUARTER_OF_MIN, TimeUnit.SECONDS);
//        Configuration.fileDownload = FileDownloadMode.PROXY;
//        Configuration.proxyEnabled = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Before(order = 2)
    public void openBrowser() {
        Selenide.open();
        printBrowserSettings();
    }

    @After(order = 3)
    public void closeBrowser() {
        Browser.closeBrowser();
        LOGGER.info("Browser has been closed");
    }

    @After(order = 2)
    public void saveLogs(Scenario scenario) {
        Allure.addAttachment("URL", Browser.getCurrentUrl());
        getScreenshotBytes()
                .ifPresent(bytes -> Allure.getLifecycle()
                        .addAttachment("Screenshot", "image/png", "png", bytes));
        attachVideo(scenario);
    }
}