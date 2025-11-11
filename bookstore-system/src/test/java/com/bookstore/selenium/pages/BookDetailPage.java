package com.bookstore.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BookDetailPage extends BasePage {

    @FindBy(tagName = "h1")
    private WebElement bookTitle;

    @FindBy(className = "book-author")
    private WebElement bookAuthor;

    @FindBy(className = "book-price")
    private WebElement bookPrice;

    @FindBy(id = "quantityInput")
    private WebElement quantityInput;

    @FindBy(id = "addToCartButton")
    private WebElement addToCartButton;

    @FindBy(className = "book-description")
    private WebElement bookDescription;

    @FindBy(className = "related-books")
    private WebElement relatedBooksSection;

    public BookDetailPage(WebDriver driver) {
        super(driver);
    }

    public String getBookTitle() {
        return getElementText(bookTitle);
    }

    public String getBookAuthor() {
        return getElementText(bookAuthor);
    }

    public String getBookPrice() {
        return getElementText(bookPrice);
    }

    public String getBookDescription() {
        return getElementText(bookDescription);
    }

    public void setQuantity(int quantity) {
        sendKeysToElement(quantityInput, String.valueOf(quantity));
    }

    public void clickAddToCart() {
        clickElement(addToCartButton);
    }

    public boolean isRelatedBooksDisplayed() {
        return relatedBooksSection.isDisplayed();
    }
}