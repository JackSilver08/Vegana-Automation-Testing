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
 * Page Object Model for Shop/Products listing page
 */
public class ShopPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(xpath = "//h2[contains(text(), 'Products') or contains(text(), 'Shop')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//div[contains(@class, 'product-item') or contains(@class, 'product')]")
    private List<WebElement> productItems;

    @FindBy(xpath = "//a[contains(@href, '/addToCart')]")
    private List<WebElement> addToCartButtons;

    @FindBy(xpath = "//a[contains(@href, '/productDetail')]")
    private List<WebElement> productDetailLinks;

    @FindBy(xpath = "//input[@name='keyword' or @placeholder='Search']")
    private WebElement searchInput;

    @FindBy(xpath = "//button[contains(text(), 'Search') or @type='submit']")
    private WebElement searchButton;

    public ShopPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToShopPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/products");
    }

    public boolean isOnShopPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/products");
    }

    public int getProductCount() {
        try {
            return productItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickFirstProduct() {
        if (!productDetailLinks.isEmpty()) {
            String href = productDetailLinks.get(0).getAttribute("href");
            if (href != null && !href.isEmpty()) {
                driver.get(href);
            }
        }
    }

    public void addFirstProductToCart() {
        if (!addToCartButtons.isEmpty()) {
            String href = addToCartButtons.get(0).getAttribute("href");
            if (href != null && !href.isEmpty()) {
                driver.get(href);
            }
        }
    }

    public void searchProduct(String keyword) {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/searchProduct?keyword=" + keyword);
    }
}

