package com.java.automation.pages;

import com.java.automation.config.TestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model for Checkout page
 */
public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h2[contains(text(), 'Checkout')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//input[@placeholder='Full name' or @name='receiver']")
    private WebElement receiverInput;

    @FindBy(xpath = "//input[@placeholder='Address' or @name='address']")
    private WebElement addressInput;

    @FindBy(xpath = "//input[@placeholder='Phone Number' or @name='phone']")
    private WebElement phoneInput;

    @FindBy(xpath = "//input[@placeholder='Description' or @name='description']")
    private WebElement descriptionInput;

    @FindBy(xpath = "//form[@action='/checkout']")
    private WebElement checkoutForm;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Place Order') or contains(text(), 'Checkout')]")
    private WebElement submitButton;

    @FindBy(xpath = "//table[contains(@class, 'table-list')]//tbody//tr")
    private java.util.List<WebElement> orderItems;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToCheckoutPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/checkout");
    }

    public boolean isOnCheckoutPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/checkout");
    }

    public void fillCheckoutForm(String receiver, String address, String phone, String description) {
        wait.until(ExpectedConditions.visibilityOf(receiverInput));
        receiverInput.clear();
        receiverInput.sendKeys(receiver);
        
        addressInput.clear();
        addressInput.sendKeys(address);
        
        phoneInput.clear();
        phoneInput.sendKeys(phone);
        
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
    }

    public void submitCheckout() {
        wait.until(ExpectedConditions.visibilityOf(checkoutForm));
        checkoutForm.submit();
    }

    public int getOrderItemCount() {
        try {
            return orderItems.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

