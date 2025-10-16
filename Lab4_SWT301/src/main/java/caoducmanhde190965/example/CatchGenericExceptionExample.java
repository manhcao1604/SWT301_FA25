package caoducmanhde190965.example;

public class CatchGenericExceptionExample {
    public static void main(String[] args) {

        try {
            String s = null;
            System.out.println(s.length());
        } catch (NullPointerException e) {
            System.out.println("Error: Null value encountered.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }
}