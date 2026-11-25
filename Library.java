import java.util.ArrayList;
import java.util.Comparator;
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

    public int getToday() {
        return today;
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
        if (loansHeldBy(memberId).size() >= member.getMaxItems()) {
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

    public int advance(int days) {
        today += days;
        return today;
    }

    public List<LibraryItem> inventory() {
        return new ArrayList<>(items.values());
    }

    public List<LibraryItem> search(String text) {
        List<LibraryItem> result = new ArrayList<>();
        for (LibraryItem item : items.values()) {
            if (item.matchesTitle(text)) {
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(LibraryItem::getTitle));
        return result;
    }

    public List<Loan> loansOf(String memberId) throws MemberNotFoundException {
        if (!members.containsKey(memberId)) {
            throw new MemberNotFoundException("member " + memberId + " not found");
        }
        List<Loan> result = loansHeldBy(memberId);
        result.sort(Comparator.comparing(Loan::getDueDay)
                .thenComparing(loan -> loan.getItem().getId()));
        return result;
    }

    public List<Loan> overdue() {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loansByItemId.values()) {
            if (loan.daysLate(today) > 0) {
                result.add(loan);
            }
        }
        result.sort(Comparator.comparing((Loan loan) -> loan.daysLate(today))
                .reversed()
                .thenComparing(loan -> loan.getItem().getId()));
        return result;
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

    // Unsorted: every active loan held by this member.
    private List<Loan> loansHeldBy(String memberId) {
        List<Loan> held = new ArrayList<>();
        for (Loan loan : loansByItemId.values()) {
            if (loan.getMember().getId().equals(memberId)) {
                held.add(loan);
            }
        }
        return held;
    }
}
