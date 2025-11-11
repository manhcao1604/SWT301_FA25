package com.bookstore.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(linkText = "Books")
    private WebElement booksLink;

    @FindBy(linkText = "Categories")
    private WebElement categoriesLink;

    @FindBy(linkText = "Cart")
    private WebElement cartLink;

    @FindBy(linkText = "Login")
    private WebElement loginLink;

    @FindBy(className = "navbar-brand")
    private WebElement logo;

    @FindBy(id = "searchInput")
    private WebElement searchInput;

    @FindBy(id = "searchButton")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToBooks() {
        clickElement(booksLink);
    }

    public void navigateToCategories() {
        clickElement(categoriesLink);
    }

    public void navigateToCart() {
        clickElement(cartLink);
    }

    public void navigateToLogin() {
        clickElement(loginLink);
    }

    public void searchForBook(String bookTitle) {
        sendKeysToElement(searchInput, bookTitle);
        clickElement(searchButton);
    }

    public boolean isLogoDisplayed() {
        return logo.isDisplayed();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}