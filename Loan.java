class Loan {
    private final LibraryItem item;
    private final Member member;
    private final int dueDay;
    private final int finePerDay;

    Loan(LibraryItem item, Member member, int dueDay, int finePerDay) {
        this.item = item;
        this.member = member;
        this.dueDay = dueDay;
        this.finePerDay = finePerDay;
    }

    public LibraryItem getItem() { return item; }

    public Member getMember() { return member; }

    public int getDueDay() { return dueDay; }

    public int daysLate(int today) {
        return Math.max(0, today - dueDay);
    }

    public int fine(int today) {
        return daysLate(today) * finePerDay;
    }
}
