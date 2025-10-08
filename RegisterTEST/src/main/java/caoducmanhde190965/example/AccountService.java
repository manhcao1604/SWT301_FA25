package caoducmanhde190965.example;



import java.util.regex.Pattern;

public class AccountService {


    public boolean registerAccount(String username, String password, String email) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        if (password == null || password.length() <= 6) {
            return false;
        }
        if (!isValidEmail(email)) {
            return false;
        }
        return true;
    }


    public boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }
}
