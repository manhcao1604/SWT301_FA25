package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utils.DriverFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseTest {
    protected static WebDriver driver;

    @BeforeAll
    public static void setUpBase() {
        System.out.println("=".repeat(50));
        System.out.println(" BẮT ĐẦU CHẠY KIỂM THỬ");
        System.out.println("=".repeat(50));

        try {
            driver = DriverFactory.createDriver();
            driver.manage().window().maximize();
            System.out.println(" Khởi tạo WebDriver thành công");
        } catch (Exception e) {
            System.out.println(" Lỗi khởi tạo WebDriver: " + e.getMessage());
            throw e;
        }
    }

    @BeforeEach
    public void setUpTest(TestInfo testInfo) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(" BẮT ĐẦU TEST: " + testInfo.getDisplayName());
        System.out.println("═".repeat(50));
    }

    @AfterEach
    public void tearDownTest(TestInfo testInfo) {

        takeScreenshot(testInfo.getDisplayName());

        System.out.println(" KẾT THÚC TEST: " + testInfo.getDisplayName());
        System.out.println("═".repeat(50));
    }

    @AfterAll
    public static void tearDownBase() {
        if (driver != null) {
            driver.quit();
            System.out.println(" Đã đóng WebDriver");
        }
        System.out.println("=".repeat(50));
        System.out.println(" KẾT THÚC CHẠY KIỂM THỬ");
        System.out.println("=".repeat(50));
    }

    protected void takeScreenshot(String testName) {
        try {
            // Tạo thư mục screenshots nếu chưa tồn tại
            Path screenshotsDir = Paths.get("screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }


            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "screenshots/" + testName + "_" + timestamp + ".png";

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(fileName));
            System.out.println(" Đã lưu ảnh chụp: " + fileName);
        } catch (IOException e) {
            System.out.println(" Lỗi khi chụp ảnh: " + e.getMessage());
        }
    }

    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            System.out.println(" Đã chờ " + seconds + " giây");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}