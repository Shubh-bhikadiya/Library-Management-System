class Magazine extends LibraryItem implements Borrowable {
    private final String issue;

    Magazine(String id, String title, String issue) {
        super(id, title);
        this.issue = issue;
    }

    public String getIssue() { return issue; }

    @Override
    public ItemType getType() { return ItemType.MAGAZINE; }

    @Override
    public int getFinePerDay() { return 1; }
}
