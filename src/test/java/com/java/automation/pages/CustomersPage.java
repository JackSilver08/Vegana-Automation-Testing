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
 * Page Object Model for Admin Customers Management page
 */
public class CustomersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page Title
    @FindBy(xpath = "//h4[contains(@class, 'page-title') and contains(text(), 'Customer Management')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//h4[contains(@class, 'card-title') and contains(text(), 'Customer Management')]")
    private WebElement cardTitle;

    // Add Customer Button
    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and contains(text(), 'Thêm mới')]")
    private WebElement addCustomerButton;

    // Table elements
    @FindBy(xpath = "//table[@id='add-row']")
    private WebElement customersTable;

    @FindBy(xpath = "//table[@id='add-row']//tbody//tr")
    private java.util.List<WebElement> customerRows;

    public CustomersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToCustomersPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/customers");
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public boolean isOnCustomersPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/admin/customers");
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

    public boolean isAddCustomerButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addCustomerButton));
            return addCustomerButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCustomersTableDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(customersTable));
            return customersTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddCustomerButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addCustomerButton));
        addCustomerButton.click();
    }

    public int getCustomerCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(customersTable));
            return customerRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

