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

public class CartPage extends BasePage {

    // Page elements
    @FindBy(css = ".single-content h2")
    private WebElement cartPageHeader;

    @FindBy(css = ".breadcrumb-item.active")
    private WebElement activeBreadcrumb;

    // Cart table elements
    @FindBy(css = ".table-list thead tr")
    private WebElement cartTableHeader;

    @FindBy(css = ".table-list tbody tr")
    private List<WebElement> cartItems;

    // Individual cart item elements (using dynamic selectors)
    @FindBy(css = ".table-list tbody tr .table-name h5")
    private List<WebElement> productNames;

    @FindBy(css = ".table-list tbody tr .table-product img")
    private List<WebElement> productImages;

    @FindBy(css = ".table-list tbody tr .table-price h5")
    private List<WebElement> productPrices;

    @FindBy(css = ".table-list tbody tr .table-discount h5")
    private List<WebElement> productDiscounts;

    @FindBy(css = ".table-list tbody tr .table-quantity input[type='number']")
    private List<WebElement> quantityInputs;

    @FindBy(css = ".table-list tbody tr .table-total h5")
    private List<WebElement> totalPrices;

    @FindBy(css = ".table-list tbody tr .table-action a[href*='productDetail']")
    private List<WebElement> viewButtons;

    @FindBy(css = ".table-list tbody tr .table-action a[onclick*='showConfigModalDialog']")
    private List<WebElement> deleteButtons;

    // Empty cart message
    @FindBy(css = ".alert.alert-warning p")
    private WebElement emptyCartMessage;

    // Navigation buttons
    @FindBy(css = ".cart-back a[href='/']")
    private WebElement backToShopButton;

    @FindBy(css = ".cart-proceed a[href*='checkout']")
    private WebElement proceedToCheckoutButton;

    // Cart totals
    @FindBy(css = ".cart-totals .title")
    private WebElement cartTotalsTitle;

    @FindBy(css = ".cart-totals li:last-child span:last-child")
    private WebElement cartTotalAmount;

    // Modal dialog
    @FindBy(id = "configmationId")
    private WebElement confirmationModal;

    @FindBy(css = "#configmationId .modal-body p")
    private WebElement modalMessage;

    @FindBy(id = "yesOption")
    private WebElement modalYesButton;

    @FindBy(css = "#configmationId .btn-danger")
    private WebElement modalNoButton;

    public CartPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to cart page
     */
    public void goToCartPage() {
        driver.get("http://localhost:8080/cartlist");
        waitForPageToLoad();
    }

    /**
     * Wait for page to load completely
     */
    private void waitForPageToLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(cartPageHeader));
        } catch (Exception e) {
            // If header not found, wait for cart table
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-list")));
            } catch (Exception ex) {
                // If cart list not found, wait for any cart element
                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-part")));
                } catch (Exception exc) {
                    // Just wait for body to be present
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));
                }
            }
        }
    }

    /**
     * Get cart page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get cart page header text
     */
    public String getPageHeader() {
        try {
            return cartPageHeader.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        try {
            // Check if empty cart message is displayed
            if (emptyCartMessage.isDisplayed()) {
                return true;
            }

            // Check if cart has items
            return cartItems.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Get number of items in cart
     */
    public int getCartItemCount() {
        try {
            return cartItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get product name at specific index
     */
    public String getProductName(int index) {
        try {
            if (index < productNames.size()) {
                return productNames.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get product price at specific index
     */
    public String getProductPrice(int index) {
        try {
            if (index < productPrices.size()) {
                return productPrices.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get product quantity at specific index
     */
    public String getProductQuantity(int index) {
        try {
            if (index < quantityInputs.size()) {
                return quantityInputs.get(index).getAttribute("value");
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Update product quantity at specific index
     */
    public void updateProductQuantity(int index, String newQuantity) {
        try {
            if (index < quantityInputs.size()) {
                WebElement quantityInput = quantityInputs.get(index);
                quantityInput.clear();
                quantityInput.sendKeys(newQuantity);

                // Trigger onchange event
                quantityInput.sendKeys("\t");

                // Wait for AJAX update
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Error updating quantity: " + e.getMessage());
        }
    }

    /**
     * Get total price at specific index
     */
    public String getProductTotalPrice(int index) {
        try {
            if (index < totalPrices.size()) {
                return totalPrices.get(index).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click view button for specific product
     */
    public void clickViewProduct(int index) {
        try {
            if (index < viewButtons.size()) {
                viewButtons.get(index).click();
            }
        } catch (Exception e) {
            System.out.println("Error clicking view button: " + e.getMessage());
        }
    }

    /**
     * Click delete button for specific product
     */
    public void clickDeleteProduct(int index) {
        try {
            if (index < deleteButtons.size()) {
                deleteButtons.get(index).click();
                Thread.sleep(500); // Wait for modal to appear
            }
        } catch (Exception e) {
            System.out.println("Error clicking delete button: " + e.getMessage());
        }
    }

    /**
     * Check if confirmation modal is displayed
     */
    public boolean isConfirmationModalDisplayed() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(confirmationModal)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get confirmation modal message
     */
    public String getModalMessage() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(ExpectedConditions.visibilityOf(modalMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Confirm deletion in modal
     */
    public void confirmDeletion() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(modalYesButton)).click();

            // Wait for deletion to complete
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Error confirming deletion: " + e.getMessage());
        }
    }

    /**
     * Cancel deletion in modal
     */
    public void cancelDeletion() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(modalNoButton)).click();

            // Wait for modal to close
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Error canceling deletion: " + e.getMessage());
        }
    }

    /**
     * Get cart total amount
     */
    public String getCartTotalAmount() {
        try {
            return cartTotalAmount.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click back to shop button
     */
    public void clickBackToShop() {
        try {
            backToShopButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking back to shop: " + e.getMessage());
        }
    }

    /**
     * Click proceed to checkout button
     */
    public void clickProceedToCheckout() {
        try {
            proceedToCheckoutButton.click();
        } catch (Exception e) {
            System.out.println("Error clicking proceed to checkout: " + e.getMessage());
        }
    }

    /**
     * Check if proceed to checkout button is enabled
     */
    public boolean isProceedToCheckoutEnabled() {
        try {
            return proceedToCheckoutButton.isEnabled() && proceedToCheckoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get empty cart message
     */
    public String getEmptyCartMessage() {
        try {
            return emptyCartMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Debug: Print cart information
     */
    public void debugCartInfo() {
        System.out.println("=== DEBUG CART INFO ===");
        System.out.println("Page URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + getPageTitle());
        System.out.println("Page Header: " + getPageHeader());
        System.out.println("Cart Empty: " + isCartEmpty());
        System.out.println("Number of Items: " + getCartItemCount());
        System.out.println("Cart Total: " + getCartTotalAmount());

        if (!isCartEmpty()) {
            System.out.println("\nCart Items:");
            for (int i = 0; i < getCartItemCount(); i++) {
                System.out.println("  Item " + (i + 1) + ":");
                System.out.println("    Name: " + getProductName(i));
                System.out.println("    Price: " + getProductPrice(i));
                System.out.println("    Quantity: " + getProductQuantity(i));
                System.out.println("    Total: " + getProductTotalPrice(i));
            }
        }
    }
}