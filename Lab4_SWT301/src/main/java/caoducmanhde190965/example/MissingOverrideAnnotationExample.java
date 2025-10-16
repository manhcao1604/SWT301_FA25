package caoducmanhde190965.example;

class Animal {
    void speak() {
        System.out.println("Animal speaks");
    }
}

class Dog extends Animal {
    @Override
    void speak() {
        System.out.println("Dog barks");
    }
}

public class MissingOverrideAnnotationExample {
    public static void main(String[] args) {

        Animal myDog = new Dog();
        myDog.speak();
    }
}