package caoducmanhde190965.example;

interface LoginHandler {

    boolean login(String username, String password);
}
class SimpleLogin implements LoginHandler {
    @Override
    public boolean login(String username, String password) {
        return username.equals("admin") && password.equals("123");
    }
}
public class InterfaceNamingInconsistencyExample {
    public static void main(String[] args) {
        LoginHandler handler = new SimpleLogin();
        System.out.println("Admin login: " + handler.login("admin", "123"));
    }
}