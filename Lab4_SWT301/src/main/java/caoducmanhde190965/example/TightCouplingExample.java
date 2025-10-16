package caoducmanhde190965.example;

interface MessagePrinter {
    void print(String message);
}

class ConsolePrinter implements MessagePrinter {
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
record Report(MessagePrinter printer) {
    private static final String REPORT_MESSAGE = "Generating report...";
    void generate() {
        printer.print(REPORT_MESSAGE); // Sử dụng hằng số
    }
}
public class TightCouplingExample {
    public static void main(String[] args) {
        MessagePrinter myPrinter = new ConsolePrinter();
        Report report = new Report(myPrinter); // Inject dependency
        report.generate();
    }
}