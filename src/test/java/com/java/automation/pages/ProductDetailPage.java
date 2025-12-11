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
 * Page Object Model for Product Detail page
 */
public class ProductDetailPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h2[contains(@class, 'product-name') or contains(@class, 'title')]")
    private WebElement productName;

    @FindBy(xpath = "//span[contains(@class, 'price')]")
    private WebElement productPrice;

    @FindBy(xpath = "//a[contains(@href, '/addToCart')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//input[@type='number' and @name='quantity']")
    private WebElement quantityInput;

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToProductDetail(int productId) {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/productDetail?productId=" + productId);
    }

    public void addToCart() {
        try {
            String href = addToCartButton.getAttribute("href");
            if (href != null && !href.isEmpty()) {
                driver.get(href);
            }
        } catch (Exception e) {
            // Try direct URL if button not found
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("productId=")) {
                String productId = currentUrl.substring(currentUrl.indexOf("productId=") + 9);
                String baseUrl = TestConfig.getBaseUrl();
                if (baseUrl.endsWith("/")) {
                    baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                }
                driver.get(baseUrl + "/addToCart?productId=" + productId);
            }
        }
    }

    public boolean isProductNameDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(productName));
            return productName.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

