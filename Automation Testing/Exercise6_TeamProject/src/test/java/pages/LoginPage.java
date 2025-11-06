package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By usernameInput = By.name("username");
    private By passwordInput = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.cssSelector(".oxd-alert-content-text");
    private By loginForm = By.cssSelector(".orangehrm-login-form");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        System.out.println("🌐 Đang truy cập trang demo OrangeHRM...");
        navigateTo("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        waitForPageLoad();

        System.out.println(" Page Title: " + driver.getTitle());
        System.out.println(" Current URL: " + getCurrentUrl());
    }

    public void enterUsername(String username) {
        System.out.println(" Nhập username: " + username);
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        System.out.println(" Nhập password");
        type(passwordInput, password);
    }

    public void clickLogin() {
        System.out.println("🖱 Click nút login");
        click(loginButton);
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();


        waitForPageLoad();
    }

    public boolean isErrorMessageDisplayed() {
        boolean displayed = isElementVisible(errorMessage);
        System.out.println("Error message hiển thị: " + displayed);
        if (displayed) {
            System.out.println(" Nội dung lỗi: " + getErrorMessage());
        }
        return displayed;
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isLoginPageDisplayed() {
        boolean isLoginPage = isElementVisible(loginForm);
        System.out.println("📊 Trang login hiển thị: " + isLoginPage);
        return isLoginPage;
    }

    public boolean isLoginSuccessful() {
        // Kiểm tra đã chuyển hướng đến dashboard
        boolean success = getCurrentUrl().contains("dashboard");
        System.out.println("✅ Login thành công: " + success);
        return success;
    }
}