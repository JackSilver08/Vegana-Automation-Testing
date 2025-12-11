package com.java.automation.tests.admin;

import com.java.automation.base.BaseTest;
import com.java.automation.pages.CategoriesPage;
import com.java.automation.pages.EditCategoryPage;
import com.java.automation.pages.EditProductPage;
import com.java.automation.pages.EditSupplierPage;
import com.java.automation.pages.LoginOrRegisterPage;
import com.java.automation.pages.ProductsPage;
import com.java.automation.pages.SuppliersPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * All Admin Test Cases - Authentication, Dashboard, Navigation, CRUD Operations
 */
public class AdminTest extends BaseTest {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "123123";

    private String uniqueName(String prefix) {
        return prefix + System.currentTimeMillis();
    }

    private void loginAsAdmin() {
        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    // ==================== AUTH ====================

    @Test(priority = 1, description = "01. Admin - Đăng nhập thành công")
    public void testAdminLoginSuccess() {
        extentTest.log(com.aventstack.extentreports.Status.INFO,
                "Bắt đầu test đăng nhập admin với username: " + ADMIN_USERNAME);

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, ADMIN_PASSWORD);

        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/admin/home"),
                "Đăng nhập admin thành công nhưng không redirect về trang admin dashboard. URL hiện tại: " + currentUrl);

        extentTest.log(com.aventstack.extentreports.Status.PASS,
                "Đăng nhập admin thành công và đã redirect đến trang dashboard");
    }

    @Test(priority = 2, description = "02. Admin - Đăng nhập với mật khẩu sai")
    public void testAdminLoginWithWrongPassword() {
        extentTest.log(com.aventstack.extentreports.Status.INFO,
                "Bắt đầu test đăng nhập admin với mật khẩu sai");

        LoginOrRegisterPage loginPage = new LoginOrRegisterPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login(ADMIN_USERNAME, "wrong_password");

        Assert.assertTrue(loginPage.isErrorAlertDisplayed(),
                "Không hiển thị thông báo lỗi khi đăng nhập với mật khẩu sai");

        String errorText = loginPage.getErrorAlertText();
        Assert.assertTrue(errorText.contains("không chính xác") ||
                        errorText.contains("Tài khoản") ||
                        errorText.contains("sai"),
                "Thông báo lỗi không đúng: " + errorText);

        extentTest.log(com.aventstack.extentreports.Status.PASS,
                "Đăng nhập với mật khẩu sai đã hiển thị thông báo lỗi đúng");
    }

    // ==================== CRUD FLOWS ====================

    @Test(priority = 3, description = "03. Categories - Full CRUD flow với data giả")
    public void testCategoryCRUDFlow() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD category end-to-end");
        loginAsAdmin();

        CategoriesPage categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();

        String categoryName = uniqueName("AutoCat-");
        String updatedName = categoryName + "-Updated";

        categoriesPage.createCategory(categoryName);
        Assert.assertNotNull(categoriesPage.findCategoryRow(categoryName), "Không tìm thấy category vừa thêm");

        categoriesPage.clickEditForCategory(categoryName);
        EditCategoryPage editCategoryPage = new EditCategoryPage(driver);
        editCategoryPage.updateCategoryName(updatedName);

        categoriesPage = new CategoriesPage(driver);
        categoriesPage.navigateToCategoriesPage();
        Assert.assertNotNull(categoriesPage.findCategoryRow(updatedName), "Không thấy category sau khi update");

        categoriesPage.deleteCategoryByName(updatedName);
        Assert.assertNull(categoriesPage.findCategoryRow(updatedName), "Category chưa bị xóa");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Category hoàn tất với data giả");
    }

    @Test(priority = 4, description = "04. Suppliers - Full CRUD flow với data giả")
    public void testSupplierCRUDFlow() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD supplier end-to-end");
        loginAsAdmin();

        SuppliersPage suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();

        String supplierName = uniqueName("AutoSupplier-");
        String supplierEmail = supplierName.toLowerCase() + "@example.com";
        String supplierPhone = "090" + (int) (Math.random() * 1000000);

        suppliersPage.createSupplier(supplierName, supplierEmail, supplierPhone);
        Assert.assertNotNull(suppliersPage.findSupplierRow(supplierName), "Không tìm thấy supplier vừa thêm");

        suppliersPage.clickEditSupplier(supplierName);
        EditSupplierPage editSupplierPage = new EditSupplierPage(driver);

        String updatedName = supplierName + "-Updated";
        String updatedEmail = updatedName.toLowerCase() + "@example.com";
        String updatedPhone = "091" + (int) (Math.random() * 1000000);
        editSupplierPage.updateSupplier(updatedName, updatedEmail, updatedPhone);

        suppliersPage = new SuppliersPage(driver);
        suppliersPage.navigateToSuppliersPage();
        Assert.assertNotNull(suppliersPage.findSupplierRow(updatedName), "Không thấy supplier sau khi update");

        suppliersPage.deleteSupplier(updatedName);
        Assert.assertNull(suppliersPage.findSupplierRow(updatedName), "Supplier chưa bị xóa");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Supplier hoàn tất với data giả");
    }

    @Test(priority = 5, description = "05. Products - Full CRUD flow với data giả")
    public void testProductCRUDFlow() {
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Bắt đầu CRUD product end-to-end");
        loginAsAdmin();

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();

        String productName = uniqueName("AutoProduct-");
        String price = "12345";  // DOUBLE
        String quantity = "5";       // INT
        String discount = "25";     // DOUBLE
        String enteredDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE); // DATE format YYYY-MM-DD
        String description = "Auto generated product for UI test"; // VARCHAR(255)
        String imagePath = Paths.get(
                System.getProperty("user.dir"),
                "upload",
                "image",
                "coca-cola.jpg"
        ).toString();

        productsPage.createProduct(productName, price, quantity, discount, enteredDate, description, imagePath);
        Assert.assertNotNull(productsPage.findProductRow(productName), "Không tìm thấy sản phẩm vừa thêm");

        productsPage.clickEditProduct(productName);
        EditProductPage editProductPage = new EditProductPage(driver);

        String updatedName = productName + "-Updated";
        String updatedPrice = "2222275";  // DOUBLE
        String updatedQuantity = "7";       // INT
        String updatedDiscount = "3";     // DOUBLE
        String updatedDescription = "Updated auto product"; // VARCHAR(255)
        editProductPage.updateProduct(updatedName, updatedPrice, updatedQuantity, updatedDiscount, enteredDate, updatedDescription, imagePath);

        productsPage = new ProductsPage(driver);
        productsPage.navigateToProductsPage();
        Assert.assertNotNull(productsPage.findProductRow(updatedName), "Không thấy sản phẩm sau khi update");

        productsPage.deleteProduct(updatedName);
        Assert.assertNull(productsPage.findProductRow(updatedName), "Sản phẩm chưa bị xóa");

        extentTest.log(com.aventstack.extentreports.Status.PASS, "CRUD Product hoàn tất với data giả");
    }
}
