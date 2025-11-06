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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        try {
            WebElement element = waitForClickable(locator);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            WebElement element = waitForClickable(locator);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();
        }
    }

    protected void type(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected void uploadFile(By locator, String filePath) {
        WebElement fileInput = driver.findElement(locator);
        fileInput.sendKeys(filePath);
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    protected void closeAdsIfPresent() {
        try {

            WebElement closeAd = driver.findElement(By.id("close-fixedban"));
            if (closeAd.isDisplayed()) {
                js.executeScript("arguments[0].click();", closeAd);
                Thread.sleep(1000);
            }
        } catch (Exception e) {

        }
    }
}