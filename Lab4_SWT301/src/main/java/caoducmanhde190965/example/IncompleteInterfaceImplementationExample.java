package caoducmanhde190965.example;

interface Shape {
    void resize();
}

class Square implements Shape {

    @Override
    public void resize() {
        System.out.println("Resizing square");
    }
}
public class IncompleteInterfaceImplementationExample {
    public static void main(String[] args) {
        Shape mySquare = new Square();
        mySquare.resize();
    }
}