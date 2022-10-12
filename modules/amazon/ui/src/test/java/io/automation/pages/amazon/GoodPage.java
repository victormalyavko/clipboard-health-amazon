package io.automation.pages.amazon;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.automation.selenide.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class GoodPage extends BasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodPage.class);

    public static SelenideElement ADD_TO_CART_BTN = $x("//input[@id='add-to-cart-button']");
    public static SelenideElement COLOR_BTN = $x("//form[@id='twister']//li[contains(@id, 'color_name')]");
    public static SelenideElement SIZE_SELECT = $x("//select[@id='native_dropdown_selected_size_name']");
    public static SelenideElement BUYING_OPTIONS_BTN = $x("//a[contains(normalize-space(.), 'See All Buying Options')]");
    public static SelenideElement ADD_TO_CART_INNER_BTN = $x("//input[@name='submit.addToCart']");
    public static SelenideElement INNER_STATUS_ADDED_LABEL = $x("//div[@id='aod-container']//div[@class='a-alert-content' and contains(normalize-space(.), 'Added')]");
    public static SelenideElement INNER_STATUS_UPDATED_LABEL = $x("//div[@id='aod-container']//div[@class='a-alert-content' and contains(normalize-space(.), 'Updated')]");
    public static SelenideElement SUBTOTAL_PRICE_LABEL = $x("//div[@id='nav-flyout-ewc']//div[contains(@class,'subtotal')]//span[contains(@class, 'price')]");

    // TODO: Can be moved to Util storage class;
    public static SelenideElement WHOLE_PRICE_LABEL = $x("//div[@id='aod-price-1']//span[@class='a-price-whole']");
    public static SelenideElement FRACTION_PRICE_LABEL = $x("//div[@id='aod-price-1']//span[@class='a-price-fraction']");
    public static SelenideElement PRICE_SYMBOL_LABEL = $x("//div[@id='aod-price-1']//span[@class='a-price-symbol']");

    private int quantity;
    private double totalPrice;
    private double pricePerItem;
    private String currencySymbol;

    public GoodPage() {
        super();
        isClickable(ADD_TO_CART_BTN);
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPricePerItem() {
        return pricePerItem;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public GoodPage selectSmallestSize() {
        selectFirst(SIZE_SELECT);
        return this;
    }

    public GoodPage selectColor(int number) {
        SelenideElement color = $x(String.format("//form[@id='twister']//li[contains(@id, 'color_name_%d')]", number));
        return this;
    }

    public GoodPage searchAvailableGood() {
        ElementsCollection goods = $$x("//form[@id='twister']//li[contains(@id, 'color_name_')]");
        SelenideElement good;
        for (int i = 0; i < goods.size(); i++) {
            good = $x(String.format("//form[@id='twister']//li[contains(@id, 'color_name_%d')]", i));
            clickWhenEnabled(good);
            if (isGoodAvailable()) {
                LOGGER.info("Has been found AVAILABLE good");
                break;
            }
            // TODO: For analyzing here can be implemented behaviour with attaching screen regardless of the test status
        }
        return this;
    }

    public GoodPage setQuantity(int quantity) {
        clickWhenEnabled(BUYING_OPTIONS_BTN);
        clickWhenEnabled(ADD_TO_CART_INNER_BTN);
        Page.assertCondition(INNER_STATUS_ADDED_LABEL, Condition.appear);
        selectQuantity(quantity);
        return this;
    }

    public CartPage goToCart() {
        // TODO: Can be moved to ENUM nav tool class;
        SelenideElement cart_btn = $x("//div[@id='nav-tools']//div[@id='nav-cart-text-container']");
        clickWhenEnabled(cart_btn);
        return Page.at(CartPage.class);
    }

    private GoodPage selectQuantity(int quantity) {
        SelenideElement selector_btn = $x("//div[@id='aod-offer-qty-component-1']//input[@class='a-button-input' and @type='submit']");
        SelenideElement inner_quantity_select = $x(String.format("//div[@id='aod-qty-dropdown-scroller']//div//span[normalize-space(.)='%d']", quantity));
        SelenideElement close_btn = $x("//div[@id='aod-close']");
        clickWhenEnabled(selector_btn);
        clickWhenEnabled(inner_quantity_select);
        Page.assertCondition(INNER_STATUS_UPDATED_LABEL, Condition.appear);
        String priceValue = getTextWhenAvailable(WHOLE_PRICE_LABEL) + "." + getTextWhenAvailable(FRACTION_PRICE_LABEL);
        this.currencySymbol = getTextWhenAvailable(PRICE_SYMBOL_LABEL);
        this.pricePerItem = Double.parseDouble(priceValue);
        this.quantity = quantity;
        clickWhenEnabled(close_btn);
        return this;
    }

    private boolean isGoodAvailable() {
        SelenideElement hidden_wrapper = $x("//div[@id='averageCustomerReviews_feature_div']");
        SelenideElement buy_box = $x("//div[@id='buybox']");
        Page.assertCondition(
                hidden_wrapper,
                Condition.cssValue("opacity", "1"),
                Duration.ofSeconds(10)
        );
        return buy_box.getText().contains("See All Buying Options");
    }
}
