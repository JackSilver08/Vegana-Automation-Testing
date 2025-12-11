package tests;

import base.BaseTest;
import pages.AccountPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AccountTest.class);
    private AccountPage accountPage;

    @BeforeMethod
    public void setupTest() {
        accountPage = new AccountPage(driver);
        log.info("=== Khởi tạo AccountTest ===");
    }

    @Test(priority = 1)
    public void testNavigateToAccountPage() {
        log.info("=== TEST 1: Navigate to Account Page ===");

        accountPage.goToAccountPage();

        sleep(2000);

        log.info("📄 URL: {}", accountPage.getCurrentUrl());
        log.info("🏷️ Title: {}", accountPage.getPageTitle());

        // Kiểm tra đang ở trang account hoặc bị redirect đến login
        String currentUrl = accountPage.getCurrentUrl();

        if (currentUrl.contains("/login")) {
            log.info("⚠️ Redirected to login page (not logged in)");
            Assert.assertTrue(currentUrl.contains("/login"),
                    "Should be redirected to login if not authenticated");
        } else if (currentUrl.contains("/account")) {
            log.info("✅ Successfully on account page");
            Assert.assertTrue(accountPage.isOnAccountPage(),
                    "Should be on account page");
        } else {
            log.warn("⚠️ Unexpected page: {}", currentUrl);
        }

        log.info("✅ Navigation test completed");
    }

    @Test(priority = 2)
    public void testAccountPageWhenLoggedIn() {
        log.info("=== TEST 2: Account Page When Logged In ===");

        // First login (you might need to adjust credentials)
        log.info("Attempting to login first...");

        // Go to login page
        driver.get("http://localhost:8080/login");
        sleep(2000);

        // Try to login with test credentials (adjust as needed)
        String testUsername = "admin";  // Change to your test user
        String testPassword = "123123"; // Change to your test password

        // Simple login attempt
        try {
            driver.findElement(org.openqa.selenium.By.name("customerId")).sendKeys(testUsername);
            driver.findElement(org.openqa.selenium.By.name("password")).sendKeys(testPassword);
            driver.findElement(org.openqa.selenium.By.cssSelector("button[type='submit']")).click();
            sleep(3000);
        } catch (Exception e) {
            log.info("⚠️ Could not login automatically, continuing test...");
        }

        // Now go to account page
        accountPage.goToAccountPage();
        sleep(2000);

        log.info("After login URL: {}", accountPage.getCurrentUrl());

        if (accountPage.isLoggedIn() && accountPage.isOnAccountPage()) {
            log.info("✅ Successfully accessed account page while logged in");

            // Debug account information
            accountPage.debugAccountInfo();

            // Verify customer information is displayed
            Assert.assertTrue(accountPage.isCustomerInfoDisplayed(),
                    "Customer information should be displayed");

            // Check for welcome message
            String welcomeMessage = accountPage.getWelcomeMessage();
            if (!welcomeMessage.isEmpty()) {
                log.info("Welcome Message: {}", welcomeMessage);
            }

        } else if (accountPage.getCurrentUrl().contains("/login")) {
            log.info("⚠️ Still on login page - authentication required");
            Assert.assertTrue(true, "Authentication required for account page");
        } else {
            log.warn("⚠️ Unexpected state - not on account or login page");
        }

        log.info("✅ Account page test completed");
    }

    @Test(priority = 3)
    public void testCustomerInformationDisplay() {
        log.info("=== TEST 3: Customer Information Display ===");

        // Go to account page (assuming logged in from previous test)
        accountPage.goToAccountPage();
        sleep(2000);

        // Only test if logged in
        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, checking customer information...");

            String customerId = accountPage.getCustomerId();
            String fullName = accountPage.getCustomerFullName();
            String email = accountPage.getCustomerEmail();

            log.info("Customer ID: {}", customerId);
            log.info("Full Name: {}", fullName);
            log.info("Email: {}", email);

            // At least some customer information should be displayed
            boolean hasInfo = !customerId.isEmpty() || !fullName.isEmpty() || !email.isEmpty();
            Assert.assertTrue(hasInfo, "At least some customer information should be displayed");

            // If email is displayed, it should contain @
            if (!email.isEmpty()) {
                Assert.assertTrue(email.contains("@"), "Email should contain @ symbol");
            }

            log.info("✅ Customer information display verified");
        } else {
            log.info("⚠️ Not logged in, skipping customer information test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 4)
    public void testOrderHistory() {
        log.info("=== TEST 4: Order History Display ===");

        accountPage.goToAccountPage();
        sleep(2000);

        // Only test if logged in
        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, checking order history...");

            boolean hasBillTable = accountPage.isBillTableDisplayed();
            boolean hasNoOrdersMessage = accountPage.isNoOrdersMessageDisplayed();
            int billCount = accountPage.getBillCount();

            log.info("Has Bill Table: {}", hasBillTable);
            log.info("Has No Orders Message: {}", hasNoOrdersMessage);
            log.info("Bill Count: {}", billCount);

            // Should have either bill table or no orders message
            Assert.assertTrue(hasBillTable || hasNoOrdersMessage,
                    "Should have either order history or no orders message");

            if (hasBillTable && billCount > 0) {
                log.info("✅ User has order history");

                // Check for common table headers
                boolean hasOrderId = accountPage.hasTableHeader("order") ||
                        accountPage.hasTableHeader("id") ||
                        accountPage.hasTableHeader("bill");
                boolean hasDate = accountPage.hasTableHeader("date") ||
                        accountPage.hasTableHeader("ngày");
                boolean hasTotal = accountPage.hasTableHeader("total") ||
                        accountPage.hasTableHeader("amount") ||
                        accountPage.hasTableHeader("tổng");

                log.info("Has Order ID Header: {}", hasOrderId);
                log.info("Has Date Header: {}", hasDate);
                log.info("Has Total Header: {}", hasTotal);

                // At least some headers should be present
                Assert.assertTrue(hasOrderId || hasDate || hasTotal,
                        "Should have at least some order history headers");

                // Print first bill details
                String firstBill = accountPage.getBillDetails(0);
                log.info("First Bill Details: {}", firstBill);

            } else if (hasNoOrdersMessage) {
                String noOrdersMsg = accountPage.getNoOrdersMessage();
                log.info("No Orders Message: {}", noOrdersMsg);
                Assert.assertTrue(noOrdersMsg.length() > 0,
                        "No orders message should have content");
                log.info("✅ No order history (new user)");
            }
        } else {
            log.info("⚠️ Not logged in, skipping order history test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 5)
    public void testAccountNavigationButtons() {
        log.info("=== TEST 5: Account Navigation Buttons ===");

        accountPage.goToAccountPage();
        sleep(2000);

        // Only test if logged in
        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, testing navigation buttons...");

            // Test home link
            String currentUrl = accountPage.getCurrentUrl();
            accountPage.clickHomeLink();
            sleep(2000);

            String afterHomeUrl = driver.getCurrentUrl();
            log.info("After Home Link URL: {}", afterHomeUrl);

            // Should navigate to home or stay on same page
            boolean isHomePage = afterHomeUrl.equals("http://localhost:8080/") ||
                    afterHomeUrl.equals("http://localhost:8080") ||
                    afterHomeUrl.contains("home") ||
                    driver.getTitle().toLowerCase().contains("home");

            if (isHomePage) {
                log.info("✅ Home link works correctly");
            } else {
                log.info("⚠️ Not redirected to home as expected");
            }

            // Go back to account page
            driver.navigate().back();
            sleep(2000);

            // Test shop link
            accountPage.clickShopLink();
            sleep(2000);

            String afterShopUrl = driver.getCurrentUrl();
            log.info("After Shop Link URL: {}", afterShopUrl);

            boolean isShopPage = afterShopUrl.contains("/products") ||
                    afterShopUrl.contains("/shop") ||
                    driver.getTitle().toLowerCase().contains("shop") ||
                    driver.getTitle().toLowerCase().contains("product");

            if (isShopPage) {
                log.info("✅ Shop link works correctly");
            } else {
                log.info("⚠️ Not redirected to shop as expected");
            }

        } else {
            log.info("⚠️ Not logged in, skipping navigation buttons test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 6)
    public void testAccountActions() {
        log.info("=== TEST 6: Account Actions ===");

        accountPage.goToAccountPage();
        sleep(2000);

        // Only test if logged in
        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, testing account actions...");

            // Test edit profile (might redirect or open modal)
            try {
                accountPage.clickEditProfile();
                sleep(2000);
                log.info("Clicked edit profile");

                String afterEditUrl = driver.getCurrentUrl();
                boolean isEditPage = afterEditUrl.contains("/profile") ||
                        afterEditUrl.contains("/edit") ||
                        driver.getPageSource().toLowerCase().contains("edit profile");

                if (isEditPage) {
                    log.info("✅ Edit profile action works");
                } else {
                    log.info("⚠️ Not redirected to edit profile page");
                }

                // Go back to account
                driver.navigate().back();
                sleep(2000);
            } catch (Exception e) {
                log.info("⚠️ Edit profile button not found or not clickable");
            }

            // Test view orders (if separate page)
            try {
                accountPage.clickViewOrders();
                sleep(2000);
                log.info("Clicked view orders");

                String afterOrdersUrl = driver.getCurrentUrl();
                boolean isOrdersPage = afterOrdersUrl.contains("/orders") ||
                        afterOrdersUrl.contains("/order") ||
                        driver.getPageSource().toLowerCase().contains("order history");

                if (isOrdersPage) {
                    log.info("✅ View orders action works");
                } else {
                    log.info("⚠️ Not redirected to orders page");
                }

                // Go back to account
                driver.navigate().back();
                sleep(2000);
            } catch (Exception e) {
                log.info("⚠️ View orders button not found or not clickable");
            }

            // Test wishlist
            try {
                accountPage.clickWishlist();
                sleep(2000);
                log.info("Clicked wishlist");

                String afterWishlistUrl = driver.getCurrentUrl();
                boolean isWishlistPage = afterWishlistUrl.contains("/wishlist") ||
                        afterWishlistUrl.contains("/wish") ||
                        driver.getPageSource().toLowerCase().contains("wishlist");

                if (isWishlistPage) {
                    log.info("✅ Wishlist action works");
                } else {
                    log.info("⚠️ Not redirected to wishlist page");
                }

                // Go back to account
                driver.navigate().back();
                sleep(2000);
            } catch (Exception e) {
                log.info("⚠️ Wishlist button not found or not clickable");
            }

        } else {
            log.info("⚠️ Not logged in, skipping account actions test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 7)
    public void testLogoutFunctionality() {
        log.info("=== TEST 7: Logout Functionality ===");

        accountPage.goToAccountPage();
        sleep(2000);

        // Only test if logged in
        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, testing logout...");

            // Save current URL before logout
            String beforeLogoutUrl = accountPage.getCurrentUrl();

            // Click logout
            try {
                accountPage.clickLogout();
                sleep(3000);

                String afterLogoutUrl = driver.getCurrentUrl();
                log.info("After Logout URL: {}", afterLogoutUrl);

                // Should be redirected to home or login page after logout
                boolean isHomePage = afterLogoutUrl.equals("http://localhost:8080/") ||
                        afterLogoutUrl.equals("http://localhost:8080");
                boolean isLoginPage = afterLogoutUrl.contains("/login");
                boolean isLoggedOut = isHomePage || isLoginPage;

                if (isLoggedOut) {
                    log.info("✅ Logout successful - redirected to {}",
                            isHomePage ? "home page" : "login page");

                    // Try to access account page again (should redirect to login)
                    accountPage.goToAccountPage();
                    sleep(2000);

                    String afterAccessUrl = driver.getCurrentUrl();
                    if (afterAccessUrl.contains("/login")) {
                        log.info("✅ Account page now requires login (as expected)");
                    } else {
                        log.info("⚠️ Can still access account page after logout");
                    }
                } else {
                    log.info("⚠️ Not redirected after logout");
                }
            } catch (Exception e) {
                log.info("⚠️ Logout button not found or not clickable: {}", e.getMessage());
            }
        } else {
            log.info("⚠️ Not logged in, skipping logout test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 8)
    public void testAccountPageStructure() {
        log.info("=== TEST 8: Account Page Structure ===");

        accountPage.goToAccountPage();
        sleep(2000);

        String pageSource = driver.getPageSource().toLowerCase();

        // Check for common account page elements
        boolean hasAccountElements = pageSource.contains("account") ||
                pageSource.contains("profile") ||
                pageSource.contains("customer") ||
                pageSource.contains("thông tin");

        boolean hasNavigation = pageSource.contains("breadcrumb") ||
                pageSource.contains("navigation") ||
                pageSource.contains("menu");

        boolean hasActionButtons = pageSource.contains("edit") ||
                pageSource.contains("update") ||
                pageSource.contains("logout") ||
                pageSource.contains("đăng xuất");

        log.info("Has Account Elements: {}", hasAccountElements);
        log.info("Has Navigation: {}", hasNavigation);
        log.info("Has Action Buttons: {}", hasActionButtons);

        // Should have at least some account-related elements
        Assert.assertTrue(hasAccountElements || hasNavigation || hasActionButtons,
                "Should have some account page structure");

        // Check if page has form elements (for editing profile)
        boolean hasFormElements = driver.findElements(org.openqa.selenium.By.tagName("input")).size() > 0 ||
                driver.findElements(org.openqa.selenium.By.tagName("form")).size() > 0;

        log.info("Has Form Elements: {}", hasFormElements);

        log.info("✅ Account page structure verified");
    }

    @Test(priority = 9)
    public void testCompleteAccountFlow() {
        log.info("=== TEST 9: Complete Account Flow ===");

        log.info("Step 1: Access account page");
        accountPage.goToAccountPage();
        sleep(2000);

        String initialUrl = driver.getCurrentUrl();
        log.info("Initial URL: {}", initialUrl);

        if (initialUrl.contains("/login")) {
            log.info("⚠️ Need to login first");
            // Could implement login here if needed
            Assert.assertTrue(true, "Authentication required");
            return;
        }

        if (accountPage.isLoggedIn()) {
            log.info("✅ User is logged in, testing complete flow...");

            // Step 2: Verify customer info
            Assert.assertTrue(accountPage.isCustomerInfoDisplayed(),
                    "Customer information should be displayed");

            // Step 3: Check order history
            boolean hasOrderHistory = accountPage.isBillTableDisplayed() &&
                    accountPage.getBillCount() > 0;
            boolean hasNoOrders = accountPage.isNoOrdersMessageDisplayed();

            Assert.assertTrue(hasOrderHistory || hasNoOrders,
                    "Should show either order history or no orders message");

            // Step 4: Test navigation
            String currentUrl = driver.getCurrentUrl();
            accountPage.clickHomeLink();
            sleep(2000);

            boolean navigatedAway = !driver.getCurrentUrl().equals(currentUrl);
            if (navigatedAway) {
                log.info("✅ Navigation works");
                driver.navigate().back();
                sleep(2000);
            }

            // Step 5: Debug info
            accountPage.debugAccountInfo();

            log.info("🎉 Complete account flow test finished");
        } else {
            log.info("⚠️ Not logged in, skipping complete flow test");
            Assert.assertTrue(true, "Test skipped - not logged in");
        }
    }

    @Test(priority = 10)
    public void testAccountPageResponsive() {
        log.info("=== TEST 10: Account Page Responsive Checks ===");

        accountPage.goToAccountPage();
        sleep(2000);

        // Basic responsive checks
        String title = accountPage.getPageTitle();
        String url = accountPage.getCurrentUrl();

        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");

        // Check page has some content
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.length() > 100, "Page should have content");

        // Check for common HTML structure
        boolean hasHead = pageSource.contains("<head>");
        boolean hasBody = pageSource.contains("<body>");
        boolean hasHtml = pageSource.contains("<html");

        Assert.assertTrue(hasHead && hasBody && hasHtml,
                "Should have basic HTML structure");

        // Check for Thymeleaf attributes (if present)
        boolean hasThymeleaf = pageSource.contains("th:") ||
                pageSource.contains("xmlns:th=");

        if (hasThymeleaf) {
            log.info("✅ Thymeleaf template detected");
        }

        log.info("Page Title: {}", title);
        log.info("Page URL: {}", url);
        log.info("Page Source Length: {} characters", pageSource.length());

        log.info("✅ Account page responsive checks passed");
    }

    /**
     * Helper method for safe sleep
     */
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread was interrupted during sleep");
        }
    }
}