package com.java.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for edit category screen.
 */
public class EditCategoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "categoryId")
    private WebElement categoryIdInput;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Update')]")
    private WebElement updateButton;

    public EditCategoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        PageFactory.initElements(driver, this);
    }

    public String getCategoryId() {
        wait.until(ExpectedConditions.visibilityOf(categoryIdInput));
        return categoryIdInput.getAttribute("value");
    }

    public void updateCategoryName(String newName) {
        wait.until(ExpectedConditions.visibilityOf(nameInput));
        nameInput.clear();
        nameInput.sendKeys(newName);
        updateButton.click();
    }
}

