package io.automation.amazon.shops;

import com.codeborne.selenide.SelenideElement;
import io.automation.amazon.shops.electronics.tv.TVAudioCamerasMenu;
import io.automation.selenide.Page;

public interface Navigation extends Page {

    default void selectShopByCategory(String category) {
        SelenideElement element = ShopMenu.getShop(category);
        clickWhenEnabled(element);
    }

    default void selectTargetElectronicCategory(String category, String target) {
        selectShopByCategory(category);
        SelenideElement element = TVAudioCamerasMenu.getTargetLink(target);
        clickWhenEnabled(element);
    }

    default void selectAppliancesCategory(String category, String target) {
        // TODO: here idea should be clear
        SelenideElement element = null;
        selectShopByCategory(category);
        clickWhenEnabled(element);
    }
}
