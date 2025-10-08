import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import caoducmanhde190965.example.AccountService;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @ParameterizedTest(name = "Test {index}: username={0}, password={1}, email={2} => expected={3}")
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    @DisplayName("Test registerAccount với dữ liệu từ file CSV")
    void testRegisterAccountFromCsv(String username, String password, String email, boolean expected) {
        boolean result = accountService.registerAccount(username, password, email);
        assertEquals(expected, result,
                () -> "Expected " + expected + " but got " + result +
                        " for username=" + username + ", password=" + password + ", email=" + email);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    @DisplayName("Test isValidEmail riêng biệt")
    void testIsValidEmailFromCsv(String username, String password, String email, boolean expected) {

        boolean isValid = accountService.isValidEmail(email);
        if (email.contains("@")) {
            assertTrue(isValid, "Email có @ thì phải hợp lệ: " + email);
        } else {
            assertFalse(isValid, "Email không có @ thì phải không hợp lệ: " + email);
        }
    }
}
