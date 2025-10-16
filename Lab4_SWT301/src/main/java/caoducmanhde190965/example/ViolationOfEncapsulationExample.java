package caoducmanhde190965.example;
class User {
    private final String name;
    private int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age) {
        if (age > 0 && age < 120) {
            this.age = age;
        } else {
            System.err.println("Invalid age value ignored.");
        }
    }

    public void display() {
        System.out.println("User Name: " + name + ", Age: " + age);
    }
}
public class ViolationOfEncapsulationExample {
    public static void main(String[] args) {
        User user = new User("Alice", 30);
        user.display();
        user.setAge(31);
        user.setAge(-5);
        user.display();
    }
}