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

public class LoginOrRegisterPage extends BasePage {

    // Login tab elements
    @FindBy(css = "a[href='#signin']")
    private WebElement signInTab;

    @FindBy(css = "a[href='#signup']")
    private WebElement signUpTab;

    // Login form elements
    @FindBy(css = "#signin input[name='customerId']")
    private WebElement loginCustomerIdInput;

    @FindBy(css = "#signin input[name='password']")
    private WebElement loginPasswordInput;

    @FindBy(css = "#signin button[type='submit']")
    private WebElement signInButton;

    @FindBy(css = "#signin .alert-danger")
    private WebElement loginErrorAlert;

    @FindBy(css = "#signin .alert-success")
    private WebElement loginSuccessAlert;

    // Register form elements
    @FindBy(css = "#signup input[name='customerId']")
    private WebElement registerCustomerIdInput;

    @FindBy(css = "#signup input[name='fullname']")
    private WebElement registerFullnameInput;

    @FindBy(css = "#signup input[name='email']")
    private WebElement registerEmailInput;

    @FindBy(css = "#signup input[name='password']")
    private WebElement registerPasswordInput;

    @FindBy(css = "#signup #signup-check")
    private WebElement agreeCheckbox;

    @FindBy(css = "#signup button[type='submit']")
    private WebElement signUpButton;

    @FindBy(css = "#signup .alert-danger")
    private WebElement registerErrorAlert;

    @FindBy(css = "#signup .alert-success")
    private WebElement registerSuccessAlert;

    // Common elements
    @FindBy(css = ".back-arrow a[href='/']")
    private WebElement backToHomeLink;

    public LoginOrRegisterPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to login/register page
     */
    public void goToLoginPage() {
        driver.get("http://localhost:8080/login");
        waitForPageToLoad();
    }

    /**
     * Wait for page to load completely
     */
    private void waitForPageToLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(signInTab));
        } catch (Exception e) {
            // If tab not found, wait for any form
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.visibilityOf(loginCustomerIdInput));
            } catch (Exception ex) {
                // If no login form, wait for register form
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.visibilityOf(registerCustomerIdInput));
                } catch (Exception exc) {
                    // Just wait for body to be present
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                }
            }
        }
    }

    /**
     * Switch to Login tab
     */
    public void switchToLoginTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(signInTab)).click();
            Thread.sleep(500); // Wait for tab switch animation
            System.out.println("Switched to Login tab");
        } catch (Exception e) {
            System.out.println("Error switching to login tab: " + e.getMessage());
        }
    }

    /**
     * Switch to Register tab
     */
    public void switchToRegisterTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(signUpTab)).click();
            Thread.sleep(500); // Wait for tab switch animation
            System.out.println("Switched to Register tab");
        } catch (Exception e) {
            System.out.println("Error switching to register tab: " + e.getMessage());
        }
    }

    /**
     * Login with customer ID and password
     */
    public void login(String customerId, String password) {
        // Ensure we're on login tab
        switchToLoginTab();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Enter customer ID
        wait.until(ExpectedConditions.visibilityOf(loginCustomerIdInput));
        loginCustomerIdInput.clear();
        loginCustomerIdInput.sendKeys(customerId);
        System.out.println("Entered customer ID: " + customerId);

        // Enter password
        wait.until(ExpectedConditions.visibilityOf(loginPasswordInput));
        loginPasswordInput.clear();
        loginPasswordInput.sendKeys(password);
        System.out.println("Entered password");

        // Click sign in button
        wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signInButton.click();
        System.out.println("Clicked sign in button");
    }

    /**
     * Register a new user
     */
    public void register(String customerId, String fullname, String email, String password) {
        // Switch to register tab
        switchToRegisterTab();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Enter customer ID
        wait.until(ExpectedConditions.visibilityOf(registerCustomerIdInput));
        registerCustomerIdInput.clear();
        registerCustomerIdInput.sendKeys(customerId);
        System.out.println("Entered register customer ID: " + customerId);

        // Enter full name
        wait.until(ExpectedConditions.visibilityOf(registerFullnameInput));
        registerFullnameInput.clear();
        registerFullnameInput.sendKeys(fullname);
        System.out.println("Entered full name: " + fullname);

        // Enter email
        wait.until(ExpectedConditions.visibilityOf(registerEmailInput));
        registerEmailInput.clear();
        registerEmailInput.sendKeys(email);
        System.out.println("Entered email: " + email);

        // Enter password
        wait.until(ExpectedConditions.visibilityOf(registerPasswordInput));
        registerPasswordInput.clear();
        registerPasswordInput.sendKeys(password);
        System.out.println("Entered register password");

        // Check agree checkbox
        try {
            if (!agreeCheckbox.isSelected()) {
                agreeCheckbox.click();
                System.out.println("Checked agree checkbox");
            }
        } catch (Exception e) {
            System.out.println("Checkbox not found or not clickable");
        }

        // Click sign up button
        wait.until(ExpectedConditions.elementToBeClickable(signUpButton));
        signUpButton.click();
        System.out.println("Clicked sign up button");
    }

    /**
     * Check if login error is displayed
     */
    public boolean isLoginErrorDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(loginErrorAlert)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get login error message
     */
    public String getLoginErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(loginErrorAlert)).getText().trim();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    /**
     * Check if register error is displayed
     */
    public boolean isRegisterErrorDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(registerErrorAlert)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get register error message
     */
    public String getRegisterErrorMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(registerErrorAlert)).getText().trim();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    /**
     * Check if register success message is displayed
     */
    public boolean isRegisterSuccessDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(registerSuccessAlert)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get register success message
     */
    public String getRegisterSuccessMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(registerSuccessAlert)).getText().trim();
        } catch (Exception e) {
            return "No success message found";
        }
    }

    /**
     * Check if currently on login tab
     */
    public boolean isOnLoginTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            // Check if login form is visible
            return loginCustomerIdInput.isDisplayed() &&
                    loginCustomerIdInput.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if currently on register tab
     */
    public boolean isOnRegisterTab() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            // Check if register form is visible
            return registerCustomerIdInput.isDisplayed() &&
                    registerCustomerIdInput.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if login was successful (redirected to home page)
     */
    public boolean isLoginSuccessful() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.contains("/login") &&
                    (currentUrl.equals("http://localhost:8080/") ||
                            currentUrl.equals("http://localhost:8080"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Debug: Print current page state
     */
    public void debugPageState() {
        System.out.println("=== DEBUG PAGE STATE ===");
        System.out.println("URL: " + getCurrentUrl());
        System.out.println("Title: " + getPageTitle());
        System.out.println("On Login Tab: " + isOnLoginTab());
        System.out.println("On Register Tab: " + isOnRegisterTab());
        System.out.println("Login Error Displayed: " + isLoginErrorDisplayed());
        System.out.println("Register Error Displayed: " + isRegisterErrorDisplayed());
        System.out.println("Register Success Displayed: " + isRegisterSuccessDisplayed());
    }
}