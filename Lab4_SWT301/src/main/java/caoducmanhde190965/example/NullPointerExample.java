package caoducmanhde190965.example;

public class NullPointerExample {
    public static void main(String[] args) {
        String text = null;

        if (text != null && !text.isEmpty()) {
            System.out.println("text is not empty");
        }
    }
}