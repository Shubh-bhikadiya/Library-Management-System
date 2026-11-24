public class Main {
    public static void main(String[] args) {
        LibraryItem[] items = {
            new Book("B1", "Introduction to Algorithms", "Cormen"),
            new Magazine("M1", "Resonance", "Oct 2026"),
            new ReferenceBook("R1", "Mahabharata", "Vyasa")
        };
        Member[] members = {
            new Student("S1", "Ananya Rao"),
            new Staff("T1", "Ravi Kumar"),
            new Faculty("F1", "Meera Iyer")
        };

        for (LibraryItem item : items) {
            System.out.print(item.getId() + " " + item.getType() + " ");
            if (item instanceof Borrowable b) {
                System.out.println("fine/day " + b.getFinePerDay());
            } else {
                System.out.println("not borrowable");
            }
        }

        for (Member m : members) {
            System.out.println(m.getId() + " max " + m.getMaxItems()
                    + " days " + m.getLoanPeriod());
        }
    }
}
