package tests;

import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.DashboardPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TEST 1: Đăng nhập thành công với credentials hợp lệ")
public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    @DisplayName("Admin đăng nhập thành công và chuyển hướng đến dashboard")
    void testAdminLoginSuccess() {

        System.out.println(" Điều kiện: Ở trang đăng nhập");
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageDisplayed(),
                " PHẢI ở trang đăng nhập trước khi test");


        System.out.println("Hành động: Đăng nhập với credentials hợp lệ");
        loginPage.login("Admin", "admin123");

        System.out.println("Kiểm tra: Chuyển hướng đến dashboard thành công");

        assertTrue(loginPage.isLoginSuccessful(),
                "PHẢI chuyển hướng đến dashboard sau khi đăng nhập thành công");


        assertTrue(dashboardPage.isDashboardDisplayed(),
                " PHẢI hiển thị dashboard sau khi đăng nhập thành công");

        String profileName = dashboardPage.getProfileName();
        assertNotNull(profileName, " PHẢI hiển thị tên người dùng");

        System.out.println(" TEST 1 PASSED: Đăng nhập thành công!");
        System.out.println(" Tên người dùng: " + profileName);
    }
}