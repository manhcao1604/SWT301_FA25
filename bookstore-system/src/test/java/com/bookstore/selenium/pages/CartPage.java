package com.bookstore.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends BasePage {

    @FindBy(className = "cart-item")
    private List<WebElement> cartItems;

    @FindBy(className = "item-quantity")
    private List<WebElement> quantityInputs;

    @FindBy(className = "update-quantity")
    private List<WebElement> updateButtons;

    @FindBy(className = "remove-item")
    private List<WebElement> removeButtons;

    @FindBy(id = "totalPrice")
    private WebElement totalPrice;

    @FindBy(id = "checkoutButton")
    private WebElement checkoutButton;

    @FindBy(id = "continueShopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "empty-cart-message")
    private WebElement emptyCartMessage;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfItemsInCart() {
        return cartItems.size();
    }

    public void updateQuantity(int itemIndex, int newQuantity) {
        if (itemIndex < quantityInputs.size()) {
            sendKeysToElement(quantityInputs.get(itemIndex), String.valueOf(newQuantity));
            clickElement(updateButtons.get(itemIndex));
        }
    }

    public void removeItem(int itemIndex) {
        if (itemIndex < removeButtons.size()) {
            clickElement(removeButtons.get(itemIndex));
        }
    }

    public void clickCheckout() {
        clickElement(checkoutButton);
    }

    public void clickContinueShopping() {
        clickElement(continueShoppingButton);
    }

    public String getTotalPrice() {
        return getElementText(totalPrice);
    }

    public boolean isEmptyCartMessageDisplayed() {
        return emptyCartMessage.isDisplayed();
    }
}