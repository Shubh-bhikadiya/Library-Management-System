import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class Library {
    private int today = 0;
    private final Map<String, LibraryItem> items = new TreeMap<>();
    private final Map<String, Member> members = new TreeMap<>();
    private final Map<String, Loan> loansByItemId = new HashMap<>();

    public void addItem(LibraryItem item) {
        items.put(item.getId(), item);
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    public int borrow(String memberId, String itemId) {
        LibraryItem item = items.get(itemId);
        Member member = members.get(memberId);
        int dueDay = today + member.getLoanPeriod();
        int fineRate = ((Borrowable) item).getFinePerDay();
        loansByItemId.put(itemId, new Loan(item, member, dueDay, fineRate));
        return dueDay;
    }

    public int returnItem(String itemId) {
        Loan loan = loansByItemId.remove(itemId);
        return loan.fine(today);
    }

    public int advance(int days) {
        today += days;
        return today;
    }

    public List<LibraryItem> inventory() {
        return new ArrayList<>(items.values());
    }

    public String statusOf(LibraryItem item) {
        Loan loan = loansByItemId.get(item.getId());
        if (loan != null) {
            return "ON_LOAN|" + loan.getMember().getId();
        }
        if (item instanceof Borrowable) {
            return "AVAILABLE";
        }
        return "NOT_BORROWABLE";
    }
}
