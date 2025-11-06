package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;

public class PracticeFormPage extends BasePage {

    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    private By firstName = By.id("firstName");
    private By lastName = By.id("lastName");
    private By email = By.id("userEmail");
    private By genderMale = By.cssSelector("label[for='gender-radio-1']");
    private By genderFemale = By.cssSelector("label[for='gender-radio-2']");
    private By genderOther = By.cssSelector("label[for='gender-radio-3']");
    private By mobile = By.id("userNumber");
    private By dateOfBirth = By.id("dateOfBirthInput");
    private By subjects = By.id("subjectsInput");
    private By sportsHobby = By.cssSelector("label[for='hobbies-checkbox-1']");
    private By readingHobby = By.cssSelector("label[for='hobbies-checkbox-2']");
    private By musicHobby = By.cssSelector("label[for='hobbies-checkbox-3']");
    private By pictureUpload = By.id("uploadPicture");
    private By currentAddress = By.id("currentAddress");
    private By stateDropdown = By.id("state");
    private By stateOption = By.xpath("//div[text()='NCR']");
    private By cityDropdown = By.id("city");
    private By cityOption = By.xpath("//div[text()='Delhi']");
    private By submitButton = By.id("submit");
    private By successMessage = By.id("example-modal-sizes-title-lg");


    public void navigateToPracticeForm() {
        navigateTo("https://demoqa.com/automation-practice-form");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        closeAdsIfPresent();

        js.executeScript("window.scrollTo(0, 200)");
    }

    public void fillFirstName(String firstName) {
        type(this.firstName, firstName);
    }

    public void fillLastName(String lastName) {
        type(this.lastName, lastName);
    }

    public void fillEmail(String email) {
        type(this.email, email);
    }

    public void selectGender(String gender) {
        closeAdsIfPresent(); // Đóng quảng cáo trước khi chọn

        switch(gender.toLowerCase()) {
            case "male":
                click(genderMale);
                break;
            case "female":
                click(genderFemale);
                break;
            case "other":
                click(genderOther);
                break;
        }
    }

    public void fillMobile(String mobile) {
        type(this.mobile, mobile);
    }

    public void fillDateOfBirth(String date) {

        System.out.println("Date of Birth field skipped for simplicity");
    }

    public void fillSubjects(String subject) {
        type(subjects, subject);
        waitForVisibility(subjects).sendKeys(Keys.ENTER);
    }

    public void selectHobbies(String[] hobbies) {
        closeAdsIfPresent();

        for(String hobby : hobbies) {
            switch(hobby.toLowerCase()) {
                case "sports":
                    click(sportsHobby);
                    break;
                case "reading":
                    click(readingHobby);
                    break;
                case "music":
                    click(musicHobby);
                    break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPicture(String filePath) {
        uploadFile(pictureUpload, filePath);
    }

    public void fillCurrentAddress(String address) {
        type(currentAddress, address);
    }

    public void selectState() {
        closeAdsIfPresent();

        js.executeScript("arguments[0].scrollIntoView(true);", waitForVisibility(stateDropdown));
        click(stateDropdown);
        click(stateOption);
    }

    public void selectCity() {
        closeAdsIfPresent();
        click(cityDropdown);
        click(cityOption);
    }

    public void submitForm() {
        closeAdsIfPresent();


        js.executeScript("arguments[0].scrollIntoView(true);", waitForVisibility(submitButton));

        js.executeScript("arguments[0].click();", waitForVisibility(submitButton));
    }

    public String getSuccessMessage() {
        try {
            return waitForVisibility(successMessage).getText();
        } catch (Exception e) {
            return "Success message not found: " + e.getMessage();
        }
    }

    public boolean isFormSubmitted() {
        try {
            return waitForVisibility(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}