package caoducmanhde190965.example;

public class OvercatchingExceptionExample {
    public static void main(String[] args) {
        try {
            int[] arr = new int[0];
            System.out.println(arr[1]);
        } catch (RuntimeException e) {
            System.out.println("Runtime error");
        }
    }
}