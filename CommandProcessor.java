import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class CommandProcessor {
    private final Library library;

    CommandProcessor(Library library) {
        this.library = library;
    }

    public void run(Scanner in) {
        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (!line.isBlank()) {
                execute(line);
            }
        }
    }

    private void execute(String line) {
        String[] f = line.split("\\|");
        switch (f[0]) {
            case "ADD_BOOK" -> {
                library.addItem(new Book(f[1], f[2], f[3]));
                System.out.println("OK");
            }
            case "ADD_MAGAZINE" -> {
                library.addItem(new Magazine(f[1], f[2], f[3]));
                System.out.println("OK");
            }
            case "ADD_REFERENCE" -> {
                library.addItem(new ReferenceBook(f[1], f[2], f[3]));
                System.out.println("OK");
            }
            case "ADD_MEMBER" -> {
                library.addMember(createMember(f[1], f[2], f[3]));
                System.out.println("OK");
            }
            case "INVENTORY" -> printInventory();
            case "BORROW" -> {
                int dueDay = library.borrow(f[1], f[2]);
                System.out.println("OK due on day " + dueDay);
            }
            case "RETURN" -> {
                int fine = library.returnItem(f[1]);
                System.out.println("OK fine " + fine);
            }
            case "ADVANCE" -> {
                int day = library.advance(Integer.parseInt(f[1]));
                System.out.println("OK today is day " + day);
            }
            default -> System.out.println("(not implemented yet) " + f[0]);
        }
    }

    private Member createMember(String id, String name, String type) {
        return switch (type) {
            case "STUDENT" -> new Student(id, name);
            case "STAFF" -> new Staff(id, name);
            case "FACULTY" -> new Faculty(id, name);
            default -> throw new IllegalArgumentException("unknown member type: " + type);
        };
    }

    private void printInventory() {
        List<String> rows = new ArrayList<>();
        for (LibraryItem item : library.inventory()) {
            rows.add(item.getId() + "|" + item.getType() + "|" + library.statusOf(item));
        }
        printReport(rows);
    }

    private void printReport(List<String> rows) {
        if (rows.isEmpty()) {
            System.out.println("NONE");
            return;
        }
        for (String row : rows) {
            System.out.println(row);
        }
    }
}
