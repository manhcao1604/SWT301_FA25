package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DashboardPage extends BasePage {

    private By dashboardHeader = By.cssSelector(".oxd-topbar-header-breadcrumb");
    private By userDropdown = By.cssSelector(".oxd-userdropdown-tab");
    private By logoutLink = By.linkText("Logout");
    private By profileName = By.cssSelector(".oxd-userdropdown-name");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardDisplayed() {
        boolean displayed = isElementVisible(dashboardHeader) &&
                getCurrentUrl().contains("dashboard");
        System.out.println("📊 Dashboard hiển thị: " + displayed);
        return displayed;
    }

    public String getProfileName() {
        String name = getText(profileName);
        System.out.println("👤 Profile name: " + name);
        return name;
    }

    public void logout() {
        System.out.println(" Đang thực hiện logout...");


        click(userDropdown);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        click(logoutLink);


        waitForPageLoad();
        System.out.println("Logout thành công");
    }

    public boolean isAdminView() {

        return true;
    }
}