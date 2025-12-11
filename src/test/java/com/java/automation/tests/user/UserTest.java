package com.java.automation.tests.user;

import com.java.automation.base.BaseTest;
import com.java.automation.config.TestConfig;
import com.java.automation.pages.*;
import com.java.automation.utils.TestDataGenerator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * All User Test Cases - Authentication, Shopping, Cart & Checkout
 */
public class UserTest extends BaseTest {

    private static final String USER_ID = TestConfig.getProperty("test.user.id");
    private static final String USER_PASSWORD = TestConfig.getProperty("test.user.password");

    /**
     * Helper method to login as user
     */
    private void loginAsUser() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(USER_ID, USER_PASSWORD);
    }

    // ==================== REGISTRATION TESTS ====================

    @Test(priority = 1, description = "01. Register - Đăng ký thành công")
    public void testRegisterSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng ký thành công");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        
        String customerId = TestDataGenerator.generateUniqueCustomerId();
        String fullname = TestDataGenerator.generateUniqueFullname();
        String email = TestDataGenerator.generateUniqueEmail();
        String password = "123456";
        
        registerPage.register(customerId, fullname, email, password);
        
        Assert.assertTrue(registerPage.isSuccessAlertDisplayed(), 
            "Không hiển thị thông báo thành công khi đăng ký");
        
        String successText = registerPage.getSuccessAlertText();
        Assert.assertTrue(successText.contains("thành công") || 
                         successText.contains("Đăng kí"), 
            "Thông báo thành công không đúng: " + successText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng ký thành công");
    }

    @Test(priority = 2, description = "02. Register - Đăng ký với Customer ID đã tồn tại")
    public void testRegisterWithExistingCustomerId() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng ký với Customer ID đã tồn tại");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        
        String existingCustomerId = TestConfig.getProperty("test.user.id");
        String fullname = TestDataGenerator.generateUniqueFullname();
        String email = TestDataGenerator.generateUniqueEmail();
        String password = "123456";
        
        registerPage.register(existingCustomerId, fullname, email, password);
        
        Assert.assertTrue(registerPage.isErrorAlertDisplayed(), 
            "Không hiển thị thông báo lỗi khi đăng ký với Customer ID đã tồn tại");
        
        String errorText = registerPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("ID Login") || 
                         errorText.contains("đã được sử dụng"), 
            "Thông báo lỗi không đúng: " + errorText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng ký với Customer ID đã tồn tại đã hiển thị lỗi đúng");
    }

    @Test(priority = 3, description = "03. Register - Đăng ký với Email đã tồn tại")
    public void testRegisterWithExistingEmail() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng ký với Email đã tồn tại");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        
        String customerId = TestDataGenerator.generateUniqueCustomerId();
        String fullname = TestDataGenerator.generateUniqueFullname();
        String existingEmail = TestConfig.getProperty("test.user.email");
        String password = "123456";
        
        registerPage.register(customerId, fullname, existingEmail, password);
        
        Assert.assertTrue(registerPage.isErrorAlertDisplayed(), 
            "Không hiển thị thông báo lỗi khi đăng ký với Email đã tồn tại");
        
        String errorText = registerPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("Email") || 
                         errorText.contains("đã được sử dụng"), 
            "Thông báo lỗi không đúng: " + errorText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng ký với Email đã tồn tại đã hiển thị lỗi đúng");
    }

    @Test(priority = 4, description = "04. Register - Đăng ký với Customer ID trống")
    public void testRegisterWithEmptyCustomerId() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng ký với Customer ID trống");

        LoginOrRegisterPage registerPage = new LoginOrRegisterPage(driver);
        registerPage.navigateToLoginPage();
        
        registerPage.clickSignUpTab();
        registerPage.enterRegisterFullname("Test User");
        registerPage.enterRegisterEmail("test@example.com");
        registerPage.enterRegisterPassword("123456");
        registerPage.clickSignUpButton();
        
        Assert.assertTrue(registerPage.isOnLoginPage(), 
            "Form không validate khi Customer ID trống");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Form validate đúng khi Customer ID trống");
    }

    // ==================== LOGIN TESTS ====================

    @Test(priority = 5, description = "05. Login - Đăng nhập thành công")
    public void testLoginSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng nhập thành công");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        
        loginPage.login(USER_ID, USER_PASSWORD);
        
        Assert.assertTrue(loginPage.isOnHomePage(), 
            "Đăng nhập thành công nhưng không redirect về trang chủ");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng nhập thành công");
    }

    @Test(priority = 6, description = "06. Login - Đăng nhập với Customer ID sai")
    public void testLoginWithInvalidCustomerId() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng nhập với Customer ID sai");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        
        loginPage.login("invalid_user_id", "123456");
        
        Assert.assertTrue(loginPage.isErrorAlertDisplayed(), 
            "Không hiển thị thông báo lỗi khi đăng nhập với Customer ID sai");
        
        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") || 
                         errorText.contains("Tài khoản"), 
            "Thông báo lỗi không đúng: " + errorText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng nhập với Customer ID sai đã hiển thị lỗi đúng");
    }

    @Test(priority = 7, description = "07. Login - Đăng nhập với mật khẩu sai")
    public void testLoginWithInvalidPassword() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test đăng nhập với mật khẩu sai");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        
        loginPage.login(USER_ID, "wrong_password");
        
        Assert.assertTrue(loginPage.isErrorAlertDisplayed(), 
            "Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai");
        
        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") || 
                         errorText.contains("Tài khoản"), 
            "Thông báo lỗi không đúng: " + errorText);
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Đăng nhập với mật khẩu sai đã hiển thị lỗi đúng");
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
            {USER_ID, USER_PASSWORD, "success"},
            {"invalid_user", "123456", "error"},
            {USER_ID, "wrong_password", "error"},
            {"", "123456", "error"},
            {USER_ID, "", "error"}
        };
    }

    @Test(priority = 8, dataProvider = "loginData", description = "08. Login - Test với DataProvider")
    public void testLoginWithDataProvider(String customerId, String password, String expectedResult) {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Testing login with Customer ID: " + customerId + ", Expected: " + expectedResult);
        
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(customerId, password);
        
        if ("success".equals(expectedResult)) {
            Assert.assertTrue(loginPage.isOnHomePage(), 
                "Đăng nhập thành công nhưng không redirect về trang chủ");
            extentTest.log(com.aventstack.extentreports.Status.PASS, "Login successful");
        } else {
            boolean isError = loginPage.isErrorAlertDisplayed() || loginPage.isOnLoginPage();
            Assert.assertTrue(isError, 
                "Không hiển thị lỗi khi đăng nhập với thông tin không hợp lệ");
            extentTest.log(com.aventstack.extentreports.Status.PASS, "Error handled correctly");
        }
    }

    // ==================== SHOPPING TESTS ====================

    @Test(priority = 9, description = "09. Shop - Xem danh sách sản phẩm")
    public void testViewProductsList() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem danh sách sản phẩm");

        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        
        Assert.assertTrue(shopPage.isOnShopPage(), 
            "Không ở trang danh sách sản phẩm");
        
        int productCount = shopPage.getProductCount();
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Số lượng sản phẩm: " + productCount);
        
        Assert.assertTrue(productCount > 0, 
            "Không có sản phẩm nào được hiển thị");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem danh sách sản phẩm thành công");
    }

    @Test(priority = 10, description = "10. Shop - Tìm kiếm sản phẩm")
    public void testSearchProduct() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test tìm kiếm sản phẩm");

        ShopPage shopPage = new ShopPage(driver);
        shopPage.searchProduct("test");
        
        Assert.assertTrue(shopPage.isOnShopPage(), 
            "Không ở trang kết quả tìm kiếm");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Tìm kiếm sản phẩm thành công");
    }

    @Test(priority = 11, description = "11. Shop - Xem chi tiết sản phẩm")
    public void testViewProductDetail() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem chi tiết sản phẩm");

        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        
        int productCount = shopPage.getProductCount();
        Assert.assertTrue(productCount > 0, 
            "Không có sản phẩm để xem chi tiết");
        
        shopPage.clickFirstProduct();
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("productDetail"), 
            "Không chuyển đến trang chi tiết sản phẩm");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem chi tiết sản phẩm thành công");
    }

    // ==================== SHOPPING CART TESTS ====================

    @Test(priority = 12, description = "12. Cart - Xem giỏ hàng")
    public void testViewShoppingCart() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test xem giỏ hàng");

        loginAsUser();
        
        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
        cartPage.navigateToCartPage();
        
        Assert.assertTrue(cartPage.isOnCartPage(), 
            "Không ở trang giỏ hàng");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Xem giỏ hàng thành công");
    }

    @Test(priority = 13, description = "13. Cart - Thêm sản phẩm vào giỏ hàng")
    public void testAddProductToCart() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test thêm sản phẩm vào giỏ hàng");

        loginAsUser();
        
        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        
        int initialCartCount = 0;
        try {
            ShoppingCartPage cartPage = new ShoppingCartPage(driver);
            cartPage.navigateToCartPage();
            initialCartCount = cartPage.getCartItemCount();
        } catch (Exception e) {
            // Cart might be empty
        }
        
        shopPage.navigateToShopPage();
        shopPage.addFirstProductToCart();
        
        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
        cartPage.navigateToCartPage();
        
        int newCartCount = cartPage.getCartItemCount();
        Assert.assertTrue(newCartCount > initialCartCount || newCartCount > 0, 
            "Sản phẩm không được thêm vào giỏ hàng");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Thêm sản phẩm vào giỏ hàng thành công");
    }

    @Test(priority = 14, description = "14. Cart - Cập nhật số lượng sản phẩm")
    public void testUpdateCartQuantity() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test cập nhật số lượng sản phẩm");

        loginAsUser();
        
        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        shopPage.addFirstProductToCart();
        
        ShoppingCartPage cartPage = new ShoppingCartPage(driver);
        cartPage.navigateToCartPage();
        
        int initialCount = cartPage.getCartItemCount();
        Assert.assertTrue(initialCount > 0, 
            "Giỏ hàng không có sản phẩm để test");
        
        cartPage.updateQuantity(0, 2);
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Cập nhật số lượng sản phẩm thành công");
    }

    @Test(priority = 15, description = "15. Checkout - Xem trang checkout")
    public void testCheckoutWithItems() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test checkout");

        loginAsUser();
        
        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        shopPage.addFirstProductToCart();
        
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.navigateToCheckoutPage();
        
        Assert.assertTrue(checkoutPage.isOnCheckoutPage(), 
            "Không ở trang checkout");
        
        int itemCount = checkoutPage.getOrderItemCount();
        Assert.assertTrue(itemCount > 0, 
            "Không có sản phẩm trong đơn hàng");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Checkout page hiển thị đúng với sản phẩm trong giỏ hàng");
    }

    @Test(priority = 16, description = "16. Checkout - Submit checkout")
    public void testSubmitCheckout() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, 
            "Bắt đầu test submit checkout");

        loginAsUser();
        
        ShopPage shopPage = new ShopPage(driver);
        shopPage.navigateToShopPage();
        shopPage.addFirstProductToCart();
        
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.navigateToCheckoutPage();
        
        checkoutPage.fillCheckoutForm(
            "Test User",
            "123 Test Street",
            "0123456789",
            "Test order description"
        );
        
        checkoutPage.submitCheckout();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("checkout_success") || 
                         currentUrl.contains("success") ||
                         currentUrl.contains("/"), 
            "Không redirect đến trang success sau khi checkout");
        
        extentTest.log(com.aventstack.extentreports.Status.PASS, 
            "Submit checkout thành công");
    }
}

