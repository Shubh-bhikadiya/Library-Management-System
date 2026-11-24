import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Library {
    private final Map<String, LibraryItem> items = new TreeMap<>();
    private final Map<String, Member> members = new TreeMap<>();

    public void addItem(LibraryItem item) {
        items.put(item.getId(), item);
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    // All items, in item-ID order (a TreeMap keeps its keys sorted).
    public List<LibraryItem> inventory() {
        return new ArrayList<>(items.values());
    }

    // Temporary version: loans don't exist yet. M2-C will check for an active loan.
    public String statusOf(LibraryItem item) {
        if (item instanceof Borrowable) {
            return "AVAILABLE";
        }
        return "NOT_BORROWABLE";
    }
}
