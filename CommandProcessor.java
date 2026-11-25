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
                try {
                    execute(line);
                } catch (LibraryException e) {
                    System.out.println("ERROR " + e.getMessage());
                }
            }
        }
    }

    private void execute(String line) throws LibraryException {
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
            case "SEARCH" -> printSearch(f[1]);
            case "LOANS" -> printLoans(f[1]);
            case "OVERDUE" -> printOverdue();
            case "INVENTORY" -> printInventory();
            default -> throw new IllegalArgumentException("unknown command: " + f[0]);
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

    private void printSearch(String text) {
        List<String> rows = new ArrayList<>();
        for (LibraryItem item : library.search(text)) {
            rows.add(item.getId() + "|" + item.getType() + "|" + item.getTitle());
        }
        printReport(rows);
    }

    private void printLoans(String memberId) throws MemberNotFoundException {
        List<String> rows = new ArrayList<>();
        for (Loan loan : library.loansOf(memberId)) {
            rows.add(loan.getItem().getId() + "|" + loan.getItem().getTitle()
                    + "|" + loan.getDueDay());
        }
        printReport(rows);
    }

    private void printOverdue() {
        int today = library.getToday();
        List<String> rows = new ArrayList<>();
        for (Loan loan : library.overdue()) {
            rows.add(loan.getItem().getId() + "|" + loan.getMember().getId()
                    + "|" + loan.daysLate(today) + "|" + loan.fine(today));
        }
        printReport(rows);
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
