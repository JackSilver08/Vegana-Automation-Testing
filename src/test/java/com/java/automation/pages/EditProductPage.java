package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for edit product screen.
 */
public class EditProductPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "categoryId")
    private WebElement categorySelect;

    @FindBy(id = "supplierId")
    private WebElement supplierSelect;

    @FindBy(id = "price")
    private WebElement priceInput;

    @FindBy(id = "quantity")
    private WebElement quantityInput;

    @FindBy(id = "discount")
    private WebElement discountInput;

    @FindBy(id = "enteredDate")
    private WebElement enteredDateInput;

    @FindBy(id = "description")
    private WebElement descriptionInput;

    @FindBy(id = "image")
    private WebElement imageInput;

    @FindBy(xpath = "//button[@type='submit' and (contains(text(), 'Update') or contains(text(), 'Add'))]")
    private WebElement updateButton;

    public EditProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        PageFactory.initElements(driver, this);
    }

    public void updateProduct(String newName, String newPrice, String newQuantity, String newDiscount, String newDate, String newDescription, String imagePath) {
        wait.until(ExpectedConditions.visibilityOf(nameInput));
        nameInput.clear();
        nameInput.sendKeys(newName);

        new Select(categorySelect).selectByIndex(0);
        new Select(supplierSelect).selectByIndex(0);

        priceInput.clear();
        priceInput.sendKeys(newPrice);

        quantityInput.clear();
        quantityInput.sendKeys(newQuantity);

        discountInput.clear();
        discountInput.sendKeys(newDiscount);

        // Set date using JavaScript to ensure proper format for input type="date"
        ((org.openqa.selenium.JavascriptExecutor) driver)
            .executeScript("arguments[0].value = arguments[1];", enteredDateInput, newDate);

        descriptionInput.clear();
        descriptionInput.sendKeys(newDescription);

        if (imagePath != null) {
            imageInput.sendKeys(imagePath);
        }

        updateButton.click();
    }
}

