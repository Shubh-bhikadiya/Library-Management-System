class Book extends LibraryItem implements Borrowable {
    private final String author;

    Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    public String getAuthor() { return author; }

    @Override
    public ItemType getType() { return ItemType.BOOK; }

    @Override
    public int getFinePerDay() { return 2; }
}
