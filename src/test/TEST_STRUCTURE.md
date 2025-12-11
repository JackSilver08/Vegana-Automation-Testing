# Cấu trúc Test Automation - Vegana Shop

## Tổng quan

Test suite được tổ chức theo module và nghiệp vụ, chia thành 2 phần chính:
- **User Module**: Test các chức năng dành cho người dùng
- **Admin Module**: Test các chức năng quản trị

## Cấu trúc Test Suite

### 1. User Module

#### 01. User Authentication
- **RegisterTest**: Test đăng ký tài khoản
  - Đăng ký thành công
  - Đăng ký với Customer ID đã tồn tại
  - Đăng ký với Email đã tồn tại
  - Đăng ký với Customer ID trống

- **LoginTest**: Test đăng nhập
  - Đăng nhập thành công
  - Đăng nhập với Customer ID sai
  - Đăng nhập với mật khẩu sai

#### 02. User Shopping
- **ShopTest**: Test chức năng mua sắm
  - Xem danh sách sản phẩm
  - Tìm kiếm sản phẩm
  - Xem chi tiết sản phẩm

#### 03. User Shopping Cart & Checkout
- **ShoppingCartTest**: Test giỏ hàng và thanh toán
  - Xem giỏ hàng
  - Thêm sản phẩm vào giỏ hàng
  - Cập nhật số lượng sản phẩm
  - Xem trang checkout
  - Submit checkout

### 2. Admin Module

#### 04. Admin Authentication
- **AdminTest**: Test đăng nhập admin
  - Đăng nhập admin thành công
  - Đăng nhập với mật khẩu sai

#### 05. Admin Dashboard
- **AdminTest**: Test dashboard
  - Kiểm tra các thành phần dashboard
  - Kiểm tra role Administrator

- **AdminFunctionsTest**: Test navigation
  - Navigation giữa các trang admin

#### 06. Admin Products Management
- **AdminFunctionsTest**: Navigation và UI
  - Navigate đến Products page
  - Kiểm tra các thành phần
  - Test nút Add Product

- **AdminCRUDTest**: CRUD operations
  - Xem danh sách sản phẩm
  - Kiểm tra nút Add Product
  - Test form thêm mới
  - Navigate đến Edit page
  - Test Delete

#### 07. Admin Categories Management
- **AdminFunctionsTest**: Navigation và UI
  - Navigate đến Categories page
  - Kiểm tra các thành phần

- **AdminCRUDTest**: CRUD operations
  - Xem danh sách danh mục
  - Kiểm tra nút Add Category
  - Navigate đến Edit page
  - Test Delete

#### 08. Admin Suppliers Management
- **AdminFunctionsTest**: Navigation và UI
  - Navigate đến Suppliers page
  - Kiểm tra các thành phần

- **AdminCRUDTest**: CRUD operations
  - Xem danh sách nhà cung cấp
  - Kiểm tra nút Add Supplier
  - Navigate đến Edit page
  - Test Delete

#### 09. Admin Orders Management
- **AdminFunctionsTest**: Navigation và UI
  - Navigate đến Orders page
  - Kiểm tra các thành phần

- **AdminCRUDTest**: CRUD operations
  - Xem danh sách đơn hàng
  - Kiểm tra link Export To Excel
  - Navigate đến Edit page
  - Test Delete

#### 10. Admin Customers Management
- **AdminFunctionsTest**: Navigation và UI
  - Navigate đến Customers page
  - Kiểm tra các thành phần

- **AdminCRUDTest**: CRUD operations
  - Xem danh sách khách hàng

## Cấu trúc File

```
src/test/java/com/java/automation/
├── base/
│   └── BaseTest.java              # Base class cho tất cả tests
├── config/
│   └── TestConfig.java            # Configuration
├── pages/                          # Page Object Models
│   ├── LoginOrRegisterPage.java
│   ├── ShopPage.java
│   ├── ProductDetailPage.java
│   ├── ShoppingCartPage.java
│   ├── CheckoutPage.java
│   ├── AdminPage.java
│   ├── ProductsPage.java
│   ├── OrdersPage.java
│   ├── CustomersPage.java
│   ├── CategoriesPage.java
│   └── SuppliersPage.java
├── tests/
│   ├── user/                       # User tests
│   │   ├── LoginTest.java
│   │   ├── RegisterTest.java
│   │   ├── ShopTest.java
│   │   └── ShoppingCartTest.java
│   └── admin/                      # Admin tests
│       ├── AdminTest.java
│       ├── AdminFunctionsTest.java
│       └── AdminCRUDTest.java
└── utils/                          # Utilities
    ├── ExtentReportManager.java
    ├── LoggerUtil.java
    └── ScreenshotUtil.java
```

## Thứ tự chạy Test

Test được sắp xếp theo thứ tự logic nghiệp vụ:

1. **User Authentication** (Register → Login)
2. **User Shopping** (Xem sản phẩm → Tìm kiếm → Chi tiết)
3. **User Cart & Checkout** (Xem giỏ hàng → Thêm → Cập nhật → Checkout)
4. **Admin Authentication** (Login admin)
5. **Admin Dashboard** (Kiểm tra dashboard)
6. **Admin Management** (Products → Categories → Suppliers → Orders → Customers)

Mỗi module quản lý có thứ tự: **READ → CREATE → UPDATE → DELETE**

## Naming Convention

- Test methods: `test[Function][Scenario]`
  - Ví dụ: `testLoginSuccess`, `testAddProductToCart`
  
- Test descriptions: `[Priority]. [Module] - [Action]`
  - Ví dụ: `01. Products - Xem danh sách sản phẩm`
  - Ví dụ: `02. Products - Kiểm tra nút Add Product`

## Best Practices

1. **Mỗi test độc lập**: Không phụ thuộc vào test khác
2. **Navigate trực tiếp**: Sử dụng URL thay vì click element
3. **Timeout ngắn**: 2 giây cho WebDriverWait
4. **Không delay**: Loại bỏ Thread.sleep, dùng ExpectedConditions
5. **ExtentReport**: Tất cả test đều log vào report
6. **Skip logic**: Test có logic skip nếu không có dữ liệu

## Chạy Test

```bash
# Chạy tất cả tests
mvn test

# Chạy theo module
mvn test -Dtest=LoginTest
mvn test -Dtest=AdminCRUDTest

# Chạy theo test suite trong testng.xml
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Report

Sau khi chạy test, report được tạo tại:
- `test-output/reports/ExtentReport_[timestamp].html`
- `target/surefire-reports/`

