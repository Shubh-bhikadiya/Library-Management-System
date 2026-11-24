import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        CommandProcessor processor = new CommandProcessor(library);
        try (Scanner in = new Scanner(System.in)) {
            processor.run(in);
        }
    }
}
