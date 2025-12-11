package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AccountPage extends BasePage {

    // Page header and title
    @FindBy(css = "h1, h2:contains('Account'), .account-title")
    private WebElement accountPageHeader;

    @FindBy(css = ".breadcrumb-item.active")
    private WebElement activeBreadcrumb;

    // Customer information elements
    @FindBy(css = "input[name='customerId'], #customerId")
    private WebElement customerIdField;

    @FindBy(css = "input[name='fullname'], #fullname")
    private WebElement fullnameField;

    @FindBy(css = "input[name='email'], #email")
    private WebElement emailField;

    @FindBy(css = "input[name='password'], #password")
    private WebElement passwordField;

    // Account actions
    @FindBy(css = "a[href*='profile'], .btn-profile")
    private WebElement editProfileButton;

    @FindBy(css = "a[href*='order'], .btn-orders")
    private WebElement viewOrdersButton;

    @FindBy(css = "a[href*='wishlist'], .btn-wishlist")
    private WebElement wishlistButton;

    @FindBy(css = "a[href*='logout'], .btn-logout")
    private WebElement logoutButton;

    // Bill/Order history table
    @FindBy(css = "table.table, table.order-table")
    private WebElement billTable;

    @FindBy(css = "table.table tbody tr, table.order-table tbody tr")
    private List<WebElement> billRows;

    @FindBy(css = "table.table th, table.order-table th")
    private List<WebElement> tableHeaders;

    // No orders message
    @FindBy(css = ".alert-info, .no-orders-message, .empty-message")
    private WebElement noOrdersMessage;

    // Welcome message
    @FindBy(xpath = "//*[contains(text(), 'Welcome') or contains(text(), 'Xin chào')]")
    private WebElement welcomeMessage;

    // Navigation links
    @FindBy(css = "a[href='/'], .home-link")
    private WebElement homeLink;

    @FindBy(css = "a[href='/products'], .shop-link")
    private WebElement shopLink;

    public AccountPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to account page
     */
    public void goToAccountPage() {
        driver.get("http://localhost:8080/account");
        waitForPageToLoad();
    }

    /**
     * Wait for page to load completely
     */
    private void waitForPageToLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/account"));
        } catch (Exception e) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
            } catch (Exception ex) {
                // If still error, just continue
                System.out.println("Page load timeout, continuing anyway");
            }
        }
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Check if on account page
     */
    public boolean isOnAccountPage() {
        try {
            String currentUrl = driver.getCurrentUrl();
            String pageTitle = driver.getTitle().toLowerCase();
            return currentUrl.contains("/account") || pageTitle.contains("account");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if logged in (not redirected to login)
     */
    public boolean isLoggedIn() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.contains("/login") && isOnAccountPage();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get customer ID from field
     */
    public String getCustomerId() {
        try {
            if (customerIdField != null && customerIdField.isDisplayed()) {
                return customerIdField.getAttribute("value");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get customer full name
     */
    public String getCustomerFullName() {
        try {
            if (fullnameField != null && fullnameField.isDisplayed()) {
                return fullnameField.getAttribute("value");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get customer email
     */
    public String getCustomerEmail() {
        try {
            if (emailField != null && emailField.isDisplayed()) {
                return emailField.getAttribute("value");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if customer information is displayed
     */
    public boolean isCustomerInfoDisplayed() {
        try {
            boolean hasCustomerId = !getCustomerId().isEmpty();
            boolean hasFullName = !getCustomerFullName().isEmpty();
            boolean hasEmail = !getCustomerEmail().isEmpty();

            return hasCustomerId || hasFullName || hasEmail;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click edit profile button
     */
    public void clickEditProfile() {
        try {
            if (editProfileButton != null && editProfileButton.isDisplayed()) {
                editProfileButton.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Edit profile button not found: " + e.getMessage());
        }
    }

    /**
     * Click view orders button
     */
    public void clickViewOrders() {
        try {
            if (viewOrdersButton != null && viewOrdersButton.isDisplayed()) {
                viewOrdersButton.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("View orders button not found: " + e.getMessage());
        }
    }

    /**
     * Click wishlist button
     */
    public void clickWishlist() {
        try {
            if (wishlistButton != null && wishlistButton.isDisplayed()) {
                wishlistButton.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Wishlist button not found: " + e.getMessage());
        }
    }

    /**
     * Click logout button
     */
    public void clickLogout() {
        try {
            if (logoutButton != null && logoutButton.isDisplayed()) {
                logoutButton.click();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.out.println("Logout button not found: " + e.getMessage());
        }
    }

    /**
     * Get number of bills/orders in history
     */
    public int getBillCount() {
        try {
            return billRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Check if bill table is displayed
     */
    public boolean isBillTableDisplayed() {
        try {
            return billTable != null && billTable.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if no orders message is displayed
     */
    public boolean isNoOrdersMessageDisplayed() {
        try {
            return noOrdersMessage != null && noOrdersMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get no orders message text
     */
    public String getNoOrdersMessage() {
        try {
            return noOrdersMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get welcome message text
     */
    public String getWelcomeMessage() {
        try {
            return welcomeMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click home link
     */
    public void clickHomeLink() {
        try {
            if (homeLink != null && homeLink.isDisplayed()) {
                homeLink.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Home link not found: " + e.getMessage());
        }
    }

    /**
     * Click shop link
     */
    public void clickShopLink() {
        try {
            if (shopLink != null && shopLink.isDisplayed()) {
                shopLink.click();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Shop link not found: " + e.getMessage());
        }
    }

    /**
     * Get bill/order details at specific index
     */
    public String getBillDetails(int index) {
        try {
            if (index >= 0 && index < billRows.size()) {
                return billRows.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if specific table header exists
     */
    public boolean hasTableHeader(String headerText) {
        try {
            for (WebElement header : tableHeaders) {
                if (header.getText().toLowerCase().contains(headerText.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Debug: Print account information
     */
    public void debugAccountInfo() {
        System.out.println("=== DEBUG ACCOUNT INFO ===");
        System.out.println("Page URL: " + getCurrentUrl());
        System.out.println("Page Title: " + getPageTitle());
        System.out.println("On Account Page: " + isOnAccountPage());
        System.out.println("Logged In: " + isLoggedIn());
        System.out.println("Customer ID: " + getCustomerId());
        System.out.println("Full Name: " + getCustomerFullName());
        System.out.println("Email: " + getCustomerEmail());
        System.out.println("Customer Info Displayed: " + isCustomerInfoDisplayed());
        System.out.println("Welcome Message: " + getWelcomeMessage());
        System.out.println("Bill Table Displayed: " + isBillTableDisplayed());
        System.out.println("Bill Count: " + getBillCount());
        System.out.println("No Orders Message Displayed: " + isNoOrdersMessageDisplayed());
        System.out.println("No Orders Message: " + getNoOrdersMessage());

        if (getBillCount() > 0) {
            System.out.println("\nBill Headers:");
            for (WebElement header : tableHeaders) {
                System.out.println("  - " + header.getText());
            }

            System.out.println("\nFirst Bill Details:");
            System.out.println("  " + getBillDetails(0));
        }
    }

    /**
     * Check if page has breadcrumb navigation
     */
    public boolean hasBreadcrumb() {
        try {
            return driver.findElements(By.cssSelector(".breadcrumb")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get active breadcrumb text
     */
    public String getActiveBreadcrumb() {
        try {
            if (activeBreadcrumb != null) {
                return activeBreadcrumb.getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Simple wait method for page navigation
     */
    public void waitForNavigation() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}