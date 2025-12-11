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
 * Page Object Model for Admin Suppliers Management page
 */
public class SuppliersPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Page Title
    @FindBy(xpath = "//h4[contains(@class, 'page-title') and contains(text(), 'Supplier Management')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//h4[contains(@class, 'card-title') and contains(text(), 'Supplier Management')]")
    private WebElement cardTitle;

    // Add Supplier Button
    @FindBy(xpath = "//button[@data-target='#addRowModal']")
    private WebElement addSupplierButton;

    @FindBy(id = "addRowModal")
    private WebElement addSupplierModal;

    @FindBy(id = "name")
    private WebElement supplierNameInput;

    @FindBy(id = "email")
    private WebElement supplierEmailInput;

    @FindBy(id = "phone")
    private WebElement supplierPhoneInput;

    @FindBy(xpath = "//div[@id='addRowModal']//button[@type='submit' and contains(text(), 'Add')]")
    private WebElement submitNewSupplierButton;

    @FindBy(id = "yesOption")
    private WebElement confirmDeleteButton;

    // Table elements
    @FindBy(xpath = "//table[@id='add-row']")
    private WebElement suppliersTable;

    @FindBy(xpath = "//table[@id='add-row']//tbody//tr")
    private java.util.List<WebElement> supplierRows;

    public SuppliersPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        PageFactory.initElements(driver, this);
    }

    public void navigateToSuppliersPage() {
        String baseUrl = TestConfig.getBaseUrl();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        driver.get(baseUrl + "/admin/suppliers");
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public boolean isOnSuppliersPage() {
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("/admin/suppliers");
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

    public boolean isAddSupplierButtonDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(addSupplierButton));
            return addSupplierButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSuppliersTableDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(suppliersTable));
            return suppliersTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickAddSupplierButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addSupplierButton));
        addSupplierButton.click();
    }

    public void createSupplier(String name, String email, String phone) {
        clickAddSupplierButton();
        wait.until(ExpectedConditions.visibilityOf(addSupplierModal));
        supplierNameInput.clear();
        supplierNameInput.sendKeys(name);
        supplierEmailInput.clear();
        supplierEmailInput.sendKeys(email);
        supplierPhoneInput.clear();
        supplierPhoneInput.sendKeys(phone);
        submitNewSupplierButton.click();
        wait.until(ExpectedConditions.invisibilityOf(addSupplierModal));
    }

    public WebElement findSupplierRow(String name) {
        try {
            return driver.findElement(org.openqa.selenium.By.xpath("//table[@id='add-row']//tr[td[normalize-space()='" + name + "']]"));
        } catch (Exception e) {
            return null;
        }
    }

    public String getSupplierId(String name) {
        WebElement row = findSupplierRow(name);
        if (row == null) return null;
        try {
            return row.findElement(org.openqa.selenium.By.xpath("./td[1]")).getText();
        } catch (Exception e) {
            return null;
        }
    }

    public void clickEditSupplier(String name) {
        WebElement row = findSupplierRow(name);
        if (row == null) throw new RuntimeException("Supplier row not found for: " + name);
        WebElement editLink = row.findElement(org.openqa.selenium.By.xpath(".//a[contains(@href, '/editSupplier/')]"));
        wait.until(ExpectedConditions.elementToBeClickable(editLink)).click();
    }

    public void deleteSupplier(String name) {
        WebElement row = findSupplierRow(name);
        if (row == null) throw new RuntimeException("Supplier row not found for: " + name);
        WebElement deleteButton = row.findElement(org.openqa.selenium.By.xpath(".//button[contains(@onclick, 'showConfigModalDialog')]"));
        deleteButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
        wait.until(ExpectedConditions.stalenessOf(row));
    }

    public int getSupplierCount() {
        try {
            wait.until(ExpectedConditions.visibilityOf(suppliersTable));
            return supplierRows.size();
        } catch (Exception e) {
            return 0;
        }
    }
}

