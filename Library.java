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

    public int borrow(String memberId, String itemId)
            throws ItemNotFoundException, MemberNotFoundException,
                   ItemNotAvailableException, BorrowLimitExceededException {
        LibraryItem item = items.get(itemId);
        if (item == null) {
            throw new ItemNotFoundException("item " + itemId + " not found");
        }
        Member member = members.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException("member " + memberId + " not found");
        }
        if (!(item instanceof Borrowable borrowable)) {
            throw new ItemNotAvailableException(itemId + " cannot be borrowed");
        }
        if (loansByItemId.containsKey(itemId)) {
            throw new ItemNotAvailableException(itemId + " is on loan");
        }
        if (countLoans(memberId) >= member.getMaxItems()) {
            throw new BorrowLimitExceededException(
                    memberId + " has reached the limit of " + member.getMaxItems());
        }
        int dueDay = today + member.getLoanPeriod();
        loansByItemId.put(itemId,
                new Loan(item, member, dueDay, borrowable.getFinePerDay()));
        return dueDay;
    }

    public int returnItem(String itemId)
            throws ItemNotFoundException, ItemNotOnLoanException {
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException("item " + itemId + " not found");
        }
        Loan loan = loansByItemId.remove(itemId);
        if (loan == null) {
            throw new ItemNotOnLoanException(itemId + " is not on loan");
        }
        return loan.fine(today);
    }

    private int countLoans(String memberId) {
        int count = 0;
        for (Loan loan : loansByItemId.values()) {
            if (loan.getMember().getId().equals(memberId)) {
                count++;
            }
        }
        return count;
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
