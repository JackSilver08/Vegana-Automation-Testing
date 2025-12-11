package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    @FindBy(css = ".navbar, nav")
    private WebElement navigationBar;

    @FindBy(xpath = "//a[contains(text(), 'Logout') or contains(text(), 'Đăng xuất')]")
    private WebElement logoutLink;

    @FindBy(xpath = "//a[contains(text(), 'Profile') or contains(text(), 'Tài khoản')]")
    private WebElement profileLink;

    @FindBy(css = ".welcome-message, .user-info")
    private WebElement welcomeMessage;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isUserLoggedIn() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(logoutLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        } catch (Exception e) {
            System.out.println("Logout link not found");
        }
    }

    public String getWelcomeMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(welcomeMessage)).getText();
        } catch (Exception e) {
            return "No welcome message found";
        }
    }
}