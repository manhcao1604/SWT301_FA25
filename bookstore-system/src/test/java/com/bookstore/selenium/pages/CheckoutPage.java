package com.bookstore.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CheckoutPage extends BasePage {

    @FindBy(id = "shippingAddress")
    private WebElement shippingAddressInput;

    @FindBy(id = "paymentMethod")
    private WebElement paymentMethodSelect;

    @FindBy(id = "customerNote")
    private WebElement customerNoteInput;

    @FindBy(id = "placeOrderButton")
    private WebElement placeOrderButton;

    @FindBy(className = "order-summary")
    private WebElement orderSummary;

    @FindBy(id = "cardNumber")
    private WebElement cardNumberInput;

    @FindBy(id = "expiryDate")
    private WebElement expiryDateInput;

    @FindBy(id = "cvv")
    private WebElement cvvInput;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterShippingAddress(String address) {
        sendKeysToElement(shippingAddressInput, address);
    }

    public void selectPaymentMethod(String method) {
        // Implementation for selecting payment method
    }

    public void enterCustomerNote(String note) {
        sendKeysToElement(customerNoteInput, note);
    }

    public void enterCardDetails(String cardNumber, String expiry, String cvv) {
        sendKeysToElement(cardNumberInput, cardNumber);
        sendKeysToElement(expiryDateInput, expiry);
        sendKeysToElement(cvvInput, cvv);
    }

    public void clickPlaceOrder() {
        clickElement(placeOrderButton);
    }

    public boolean isOrderSummaryDisplayed() {
        return orderSummary.isDisplayed();
    }
}