package com.java.automation.pages;

import com.java.automation.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object Model for Shopping Cart page
 */
public class ShoppingCartPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h2[contains(text(), 'Cartlist') or contains(text(), 'Cart')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//table[contains(@class, 'table-list')]//tbody//tr")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//input[@type='number' and contains(@id, 'quantityInput')]")
    private List<WebElement> quantityInputs;

    @FindBy(xpath = "//a[contains(@onclick, 'showConfigModalDialog') or contains(@href, 'deleteCartItem')]")
    private List<WebElement> removeButtons;

    @FindBy(xpath = "//a[contains(@href, '/checkout') or contains(text(), 'Checkout')]")
    private WebElement checkoutButton;

    @FindBy(xpath = "//h5[contains(@id, 'totalPrice')]")
    private List<WebElement> totalPriceElements;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToCartPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/carts");
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
        } catch (Exception e) {
            // Page might redirect to login if not authenticated
        }
    }

    public boolean isOnCartPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/carts");
    }

    public int getCartItemCount() {
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickCheckout() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/checkout");
    }

    public void updateQuantity(int index, int quantity) {
        if (index < quantityInputs.size()) {
            WebElement input = quantityInputs.get(index);
            String productId = input.getAttribute("data-id");
            String price = input.getAttribute("data-price");
            String name = input.getAttribute("data-name");
            String image = input.getAttribute("data-image");
            String discount = input.getAttribute("data-discount");
            
            // Update via JavaScript
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));",
                input, quantity
            );
        }
    }
}

