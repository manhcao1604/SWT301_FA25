package tests;

import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.DashboardPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TEST 3: Đăng xuất thành công")
public class LogoutTest extends BaseTest {

    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeEach
    public void setUpPages() {
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
    }

    @Test
    @DisplayName("Người dùng đăng xuất thành công và quay về trang đăng nhập")
    void testLogoutSuccess() {
        System.out.println("Điều kiện: Đã đăng nhập thành công");
        loginPage.navigateToLoginPage();
        loginPage.login("Admin", "admin123");

        assertTrue(dashboardPage.isDashboardDisplayed(),
                " PHẢI đăng nhập thành công trước khi test logout");

        System.out.println(" Đã đăng nhập thành công, chuẩn bị đăng xuất...");

        System.out.println(" Hành động: Thực hiện đăng xuất");
        dashboardPage.logout();

        System.out.println(" Kiểm tra: Quay về trang đăng nhập");


        assertTrue(loginPage.isLoginPageDisplayed(),
                "PHẢI quay về trang đăng nhập sau khi đăng xuất");

        System.out.println(" TEST 3 PASSED: Đăng xuất thành công!");
    }
}