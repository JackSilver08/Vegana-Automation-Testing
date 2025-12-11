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
 * Page Object Model for Admin Dashboard page
 */
public class AdminPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Admin Dashboard Elements
    @FindBy(xpath = "//h2[contains(text(), 'Dashboard')]")
    private WebElement dashboardTitle;

    @FindBy(xpath = "//h5[contains(text(), 'Admin Dashboard')]")
    private WebElement adminDashboardSubtitle;

    @FindBy(xpath = "//div[contains(@class, 'card-title') and contains(text(), 'Overall statistics')]")
    private WebElement overallStatisticsCard;

    @FindBy(xpath = "//div[contains(@class, 'card-title') and contains(text(), 'Total income')]")
    private WebElement totalIncomeCard;

    @FindBy(xpath = "//div[contains(@class, 'card-title') and contains(text(), 'User Statistics')]")
    private WebElement userStatisticsCard;

    @FindBy(xpath = "//div[contains(@class, 'card-title') and contains(text(), 'Daily Sales')]")
    private WebElement dailySalesCard;

    // Navigation elements
    @FindBy(xpath = "//a[contains(@href, '/admin/home')]")
    private WebElement homeLink;

    @FindBy(xpath = "//a[contains(@href, '/admin/products')]")
    private WebElement productsLink;

    @FindBy(xpath = "//a[contains(@href, '/admin/orders')]")
    private WebElement ordersLink;

    @FindBy(xpath = "//a[contains(@href, '/admin/customers')]")
    private WebElement customersLink;

    @FindBy(xpath = "//a[contains(@href, '/admin/categories')]")
    private WebElement categoriesLink;

    @FindBy(xpath = "//a[contains(@href, '/admin/suppliers')]")
    private WebElement suppliersLink;

    // User info in navbar
    @FindBy(xpath = "//span[contains(@class, 'user-level') and contains(text(), 'Administrator')]")
    private WebElement administratorRole;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    // Navigation methods
    public void navigateToAdminDashboard() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/home");
        wait.until(ExpectedConditions.visibilityOf(dashboardTitle));
    }

    // Validation methods
    public boolean isOnAdminDashboard() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/admin/home");
    }

    public boolean isDashboardTitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(dashboardTitle));
            return dashboardTitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdminDashboardSubtitleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(adminDashboardSubtitle));
            return adminDashboardSubtitle.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOverallStatisticsCardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(overallStatisticsCard));
            return overallStatisticsCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTotalIncomeCardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(totalIncomeCard));
            return totalIncomeCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserStatisticsCardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(userStatisticsCard));
            return userStatisticsCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDailySalesCardDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(dailySalesCard));
            return dailySalesCard.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdministratorRoleDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(administratorRole));
            return administratorRole.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getDashboardTitle() {
        if (isDashboardTitleDisplayed()) {
            return dashboardTitle.getText();
        }
        return "";
    }

    // Navigation methods - navigate directly to URL instead of clicking
    public void clickProductsLink() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/products");
    }

    public void clickOrdersLink() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/orders");
    }

    public void clickCustomersLink() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/customers");
    }

    public void clickCategoriesLink() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/categories");
    }

    public void clickSuppliersLink() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/suppliers");
    }
}

