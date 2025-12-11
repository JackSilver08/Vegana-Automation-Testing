package pages;

import base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;

public class ProductDetailPage extends BasePage {

    @FindBy(css = ".product-title, h1, h2")
    private WebElement productName;

    @FindBy(css = ".product-price, .price")
    private WebElement productPrice;

    @FindBy(css = ".product-description, .description")
    private WebElement productDescription;

    @FindBy(css = "#addToCartBtn, .btn-add-cart, button.add-to-cart")
    private WebElement addToCartButton;

    @FindBy(css = ".product-category, .category")
    private WebElement productCategory;

    @FindBy(css = ".product-image img")
    private WebElement productImage;

    @FindBy(css = ".suggested-products .product-item a")
    private List<WebElement> suggestedProducts;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        waitForPageToLoad();
    }

    private void waitForPageToLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(productName));
        } catch (TimeoutException e) {
            System.out.println("Product detail page không load đầy đủ");
        }
    }

    public String getProductName() {
        try {
            // Thử nhiều selectors
            String[] selectors = {
                    ".product-title", "h1", "h2",
                    "[data-testid='product-name']",
                    ".product-name", ".name", "#productName",
                    "h1.product-name", ".product-detail h1"
            };

            for (String selector : selectors) {
                try {
                    List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                    if (!elements.isEmpty()) {
                        String text = elements.get(0).getText().trim();
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (Exception e) {
                    // continue
                }
            }

            // Nếu không tìm thấy, trả về default
            return "Không tìm thấy tên sản phẩm";

        } catch (Exception e) {
            return "Không tìm thấy tên sản phẩm";
        }
    }

    public String getProductPrice() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(productPrice)).getText().trim();
        } catch (TimeoutException e) {
            return "Không tìm thấy giá";
        }
    }

    public String getProductDescription() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(productDescription)).getText().trim();
        } catch (TimeoutException e) {
            return "Không tìm thấy mô tả";
        }
    }

    public void clickAddToCart() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        } catch (TimeoutException e) {
            throw new RuntimeException("Không thể click nút Add to Cart");
        }
    }

    public String getProductCategory() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(productCategory)).getText().trim();
        } catch (TimeoutException e) {
            return "Không tìm thấy category";
        }
    }

    public List<String> getSuggestedProducts() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfAllElements(suggestedProducts));
            return suggestedProducts.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .toList();
        } catch (TimeoutException e) {
            return List.of();
        }
    }

    public String getProductImageUrl() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(productImage)).getAttribute("src");
        } catch (TimeoutException e) {
            return "Không tìm thấy ảnh sản phẩm";
        }
    }

    public boolean isAddToCartButtonDisplayed() {
        try {
            // Thử nhiều selectors cho button
            String[] buttonSelectors = {
                    "#addToCartBtn", ".btn-add-cart", "button.add-to-cart",
                    "button:contains('Add to Cart')", "a:contains('Add to Cart')",
                    "input[value*='Cart']", ".add-to-cart-btn"
            };

            for (String selector : buttonSelectors) {
                try {
                    List<WebElement> buttons = driver.findElements(By.cssSelector(selector));
                    if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
                        return true;
                    }
                } catch (Exception e) {
                    // continue
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }
    // Thêm method kiểm tra page đã load
    public boolean isPageLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            return wait.until(ExpectedConditions.visibilityOf(productName)).isDisplayed() &&
                    wait.until(ExpectedConditions.visibilityOf(productPrice)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}