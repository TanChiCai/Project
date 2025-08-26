
import java.time.format.DateTimeFormatter;


public abstract class BookTransactionDetail {
    protected String id;
    protected UserData user;
    protected Book book;
    protected String status;
    protected String imageFile;

    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BookTransactionDetail() {
       
    }
    public BookTransactionDetail(String id, UserData user, Book book, String status, String imageFile) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.status = status;
        this.imageFile = imageFile;
    }

    public UserData getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public String getStatus() {
        return status;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract String getId();

    public abstract String generateNextId();

    
    public abstract String toWord();

    @Override
    public abstract String toString();
}
