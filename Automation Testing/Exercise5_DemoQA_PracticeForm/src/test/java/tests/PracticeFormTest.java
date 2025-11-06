package tests;

import org.junit.jupiter.api.*;
import pages.PracticeFormPage;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("DemoQA Practice Form Tests")
public class PracticeFormTest extends BaseTest {

    private static PracticeFormPage practiceFormPage;

    @BeforeAll
    public static void setUp() {
        practiceFormPage = new PracticeFormPage(driver);
    }

    @Test
    @Order(1)
    @DisplayName("Should submit form successfully with minimal required fields")
    void testSubmitFormSuccessfully() {
        practiceFormPage.navigateToPracticeForm();

        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());


        practiceFormPage.fillFirstName("John");
        practiceFormPage.fillLastName("Doe");
        practiceFormPage.selectGender("Male");
        practiceFormPage.fillMobile("1234567890");

        practiceFormPage.submitForm();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String successMsg = practiceFormPage.getSuccessMessage();
        System.out.println("Success Message: " + successMsg);
        assertTrue(successMsg.contains("Thanks for submitting the form"));
    }

    @Test
    @Order(2)
    @DisplayName("Should show validation for empty required fields")
    void testRequiredFieldsValidation() {
        practiceFormPage.navigateToPracticeForm();

        practiceFormPage.submitForm();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after empty submit: " + currentUrl);


        assertTrue(currentUrl.contains("automation-practice-form"));
    }
}