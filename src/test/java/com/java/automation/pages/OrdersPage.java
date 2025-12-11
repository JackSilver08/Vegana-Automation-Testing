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
 * Page Object Model for Admin Orders Management page
 */
public class OrdersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page Title
    @FindBy(xpath = "//h4[contains(@class, 'page-title') and contains(text(), 'Order Management')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//h4[contains(@class, 'card-title') and contains(text(), 'Order Management')]")
    private WebElement cardTitle;

    // Export to Excel link
    @FindBy(xpath = "//a[contains(@href, '/export') and contains(text(), 'Export To Excel')]")
    private WebElement exportToExcelLink;

    // Table elements
    @FindBy(xpath = "//table[@id='add-row']")
    private WebElement ordersTable;

    @FindBy(xpath = "//table[@id='add-row']//tbody//tr")
    private java.util.List<WebElement> orderRows;

    public OrdersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToOrdersPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/orders");
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public boolean isOnOrdersPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/admin/orders");
    }

    public boolean isPageTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCardTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(cardTitle));
            return cardTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExportToExcelLinkDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(exportToExcelLink));
            return exportToExcelLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOrdersTableDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(ordersTable));
            return ordersTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickExportToExcelLink() {
        wait.until(ExpectedConditions.elementToBeClickable(exportToExcelLink));
        exportToExcelLink.click();
    }

    public int getOrderCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(ordersTable));
            return orderRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

