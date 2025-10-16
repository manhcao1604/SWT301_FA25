package caoducmanhde190965.example;


final class Constants {
    public static final int MAX_USERS = 100;
    private Constants() {}
}

public class InterfaceFieldModificationExample {
    public static void main(String[] args) {

        if (Constants.MAX_USERS < 500) {
            System.out.println("Max users is within limit: " + Constants.MAX_USERS);
        }

    }
}