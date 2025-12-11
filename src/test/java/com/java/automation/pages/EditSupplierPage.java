package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for edit supplier screen.
 */
public class EditSupplierPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "id")
    private WebElement supplierIdInput;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "phone")
    private WebElement phoneInput;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Update')]")
    private WebElement updateButton;

    public EditSupplierPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        PageFactory.initElements(driver, this);
    }

    public void updateSupplier(String newName, String newEmail, String newPhone) {
        wait.until(ExpectedConditions.visibilityOf(nameInput));
        nameInput.clear();
        nameInput.sendKeys(newName);

        emailInput.clear();
        emailInput.sendKeys(newEmail);

        phoneInput.clear();
        phoneInput.sendKeys(newPhone);

        updateButton.click();
    }
}

