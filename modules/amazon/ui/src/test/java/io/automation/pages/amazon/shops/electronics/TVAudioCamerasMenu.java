package io.automation.pages.amazon.shops.electronics;

import com.codeborne.selenide.SelenideElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$x;

public enum TVAudioCamerasMenu {

    TV("Televisions", $x("//a[normalize-space(.)='Televisions']")),
    HEADPHONES("Headphones", $x("//a[normalize-space(.)='Headphones']")),
    SPEAKERS("Speakers", $x("//a[normalize-space(.)='Speakers']")),
    CAMERAS("Cameras", $x("//a[normalize-space(.)='Cameras']")),
    ALL_ELECTRONICS("All Electronics", $x("//a[normalize-space(.)='All Electronics']"));

    private static final Logger LOGGER = LoggerFactory.getLogger(TVAudioCamerasMenu.class);

    private String value;
    private SelenideElement element;

    TVAudioCamerasMenu(String value, SelenideElement element) {
        this.value = value;
        this.element = element.as(value);
    }

    public static SelenideElement getTargetLink(String category) {
        for (TVAudioCamerasMenu type : TVAudioCamerasMenu.values()) {
            if (type.value.equalsIgnoreCase(category)) {
                LOGGER.info("{} category has been selected", type.value);
                return type.element;
            }
        }
        LOGGER.error("Wrong category: {}", category);
        throw new RuntimeException("Unexpected code issue");
    }
}
