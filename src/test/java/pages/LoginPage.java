package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {

    private WebDriver driver;

    // Sử dụng CSS selector linh hoạt hơn
    @FindBy(css = "input#customerId, input[name='customerId'], input[placeholder*='ID'], input[placeholder*='Tên đăng nhập']")
    private WebElement customerIdInput;

    @FindBy(css = "input#password, input[name='password'], input[type='password']")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit'], input[type='submit'], .btn-primary, .btn-login")
    private WebElement loginButton;

    @FindBy(css = ".alert, .error, .message, .text-danger, [class*='error'], [class*='message']")
    private WebElement messageElement;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String customerId, String password) {
        try {
            driver.get("http://localhost:8080/login");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));


            // Chờ trang load hoàn tất
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            // Chờ các element xuất hiện với nhiều cách tiếp cận
            waitForElement(wait, customerIdInput, "customerId input");
            waitForElement(wait, passwordInput, "password input");

            // Clear và nhập dữ liệu
            customerIdInput.clear();
            customerIdInput.sendKeys(customerId);

            passwordInput.clear();
            passwordInput.sendKeys(password);

            // Chờ và click nút login
            waitForElement(wait, loginButton, "login button");
            loginButton.click();

            // Chờ sau khi click
            Thread.sleep(2000);

        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình login: " + e.getMessage());
            throw new RuntimeException("Login failed: " + e.getMessage(), e);
        }
    }

    // Helper method để chờ element
    private void waitForElement(WebDriverWait wait, WebElement element, String elementName) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            // Thử tìm element bằng cách khác nếu PageFactory không hoạt động
            System.out.println("Không tìm thấy " + elementName + " bằng PageFactory, thử tìm thủ công...");

            // Thử các selector khác nhau
            String[] customerSelectors = {"#customerId", "[name='customerId']", "input[placeholder*='ID']"};
            String[] passwordSelectors = {"#password", "[name='password']", "[type='password']"};
            String[] buttonSelectors = {"[type='submit']", ".btn-primary", ".btn-login"};

            if (elementName.contains("customerId")) {
                findElementByMultipleSelectors(wait, customerSelectors);
            } else if (elementName.contains("password")) {
                findElementByMultipleSelectors(wait, passwordSelectors);
            } else if (elementName.contains("button")) {
                findElementByMultipleSelectors(wait, buttonSelectors);
            }
        }
    }

    // Tìm element bằng nhiều selector
    private WebElement findElementByMultipleSelectors(WebDriverWait wait, String[] selectors) {
        for (String selector : selectors) {
            try {
                return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
            } catch (Exception e) {
                System.out.println("Không tìm thấy với selector: " + selector);
            }
        }
        throw new NoSuchElementException("Không tìm thấy element với bất kỳ selector nào: " + String.join(", ", selectors));
    }

    public String getMessage() {
        try {
            WebDriverWait waitShort = new WebDriverWait(driver, Duration.ofSeconds(5));

            // Thử nhiều cách tìm message
            String[] messageSelectors = {
                    ".alert", ".error", ".message", ".text-danger",
                    "[class*='error']", "[class*='message']", ".alert-danger", ".alert-success"
            };

            for (String selector : messageSelectors) {
                try {
                    WebElement message = waitShort.until(
                            ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector))
                    );
                    String text = message.getText().trim();
                    if (!text.isEmpty()) {
                        return text;
                    }
                } catch (Exception e) {
                    // Bỏ qua và thử selector tiếp theo
                }
            }

            return "Không tìm thấy thông báo";

        } catch (Exception e) {
            return "Lỗi khi lấy thông báo: " + e.getMessage();
        }
    }


    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isLoginPage() {
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle().toLowerCase();

        return currentUrl.contains("/login") ||
                pageTitle.contains("login") ||
                pageTitle.contains("đăng nhập");
    }

    public boolean isLoginSuccessful() {
        return !isLoginPage();
    }

    // Thêm method để kiểm tra có đang ở trang chủ/dashboard không
    public boolean isOnHomePage() {
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle().toLowerCase();

        return !currentUrl.contains("/login") &&
                (currentUrl.endsWith("/") ||
                        currentUrl.contains("/home") ||
                        currentUrl.contains("/dashboard") ||
                        pageTitle.contains("trang chủ") ||
                        pageTitle.contains("home") ||
                        pageTitle.contains("dashboard"));
    }
}