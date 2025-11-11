package com.bookstore.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BookListPage extends BasePage {

    @FindBy(className = "book-card")
    private List<WebElement> bookCards;

    @FindBy(css = ".book-card .card-title")
    private List<WebElement> bookTitles;

    @FindBy(css = ".book-card .btn-primary")
    private List<WebElement> viewDetailsButtons;

    @FindBy(css = ".book-card .btn-success")
    private List<WebElement> addToCartButtons;

    @FindBy(className = "pagination")
    private WebElement pagination;

    @FindBy(id = "sortSelect")
    private WebElement sortSelect;

    public BookListPage(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfBooksDisplayed() {
        return bookCards.size();
    }

    public void clickViewDetailsForBook(int index) {
        if (index < viewDetailsButtons.size()) {
            clickElement(viewDetailsButtons.get(index));
        }
    }

    public void clickAddToCartForBook(int index) {
        if (index < addToCartButtons.size()) {
            clickElement(addToCartButtons.get(index));
        }
    }

    public String getBookTitle(int index) {
        if (index < bookTitles.size()) {
            return getElementText(bookTitles.get(index));
        }
        return "";
    }

    public void sortBy(String sortOption) {
        // Implementation for sorting
    }

    public boolean isPaginationDisplayed() {
        return pagination.isDisplayed();
    }
}