package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement waitForVisibility(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println(" Element not visible: " + locator);
            throw e;
        }
    }

    protected void click(By locator) {
        try {
            WebElement element = waitForVisibility(locator);

            js.executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();
        } catch (ElementClickInterceptedException e) {

            WebElement element = waitForVisibility(locator);
            js.executeScript("arguments[0].click();", element);
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    protected boolean isElementVisible(By locator) {
        try {
            return waitForVisibility(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void navigateTo(String url) {
        try {
            driver.get(url);
            wait.until(webDriver ->
                    js.executeScript("return document.readyState").equals("complete"));
        } catch (WebDriverException e) {
            System.out.println(" Lỗi kết nối đến: " + url);
            System.out.println(" Hãy chắc chắn ứng dụng đang chạy và URL đúng");
            throw e;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void waitForPageLoad() {
        wait.until(webDriver ->
                js.executeScript("return document.readyState").equals("complete"));
    }
}