package tests;

import org.junit.jupiter.api.*;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TEST 2: Đăng nhập thất bại với credentials không hợp lệ")
public class LoginFailureTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    public void setUpPages() {
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Hiển thị thông báo lỗi khi đăng nhập với password sai")
    void testLoginInvalidPassword() {

        System.out.println(" Điều kiện: Ở trang đăng nhập");
        loginPage.navigateToLoginPage();
        assertTrue(loginPage.isLoginPageDisplayed(),
                " PHẢI ở trang đăng nhập trước khi test");


        System.out.println(" Hành động: Đăng nhập với password sai");
        loginPage.login("Admin", "wrongpassword");


        System.out.println(" Kiểm tra: Hiển thị thông báo lỗi");


        assertTrue(loginPage.isErrorMessageDisplayed(),
                "✅ PHẢI hiển thị thông báo lỗi khi credentials sai");


        String errorMessage = loginPage.getErrorMessage();
        assertTrue(errorMessage.contains("Invalid") || errorMessage.contains("Invalid credentials"),
                "✅ Thông báo lỗi phải chứa từ khóa 'Invalid'");

        System.out.println("🎉 TEST 2 PASSED: Đăng nhập thất bại đúng cách!");
        System.out.println("📊 Thông báo lỗi: " + errorMessage);
    }
}