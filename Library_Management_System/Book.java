
import java.util.Objects;

public class Book {
    private String bookID, bookName, author, genre,publisher, language;
    private String isbn;
    private int numberOfPages, publicationYear;
    private boolean isAvailable;

    public Book() {
    }


    public Book(String bookID, String bookName, String author,String isbn,String genre,String publisher, String language, int numberOfPages, int publicationYear, boolean isAvailable) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.author = author;
        this.isbn=isbn;
        this.genre = genre;
        this.publisher=publisher;
        this.language = language;
        this.numberOfPages = numberOfPages;
        this.publicationYear = publicationYear;
        this.isAvailable = isAvailable;
    }

    
    // Getters and Setters for each attribute

    public String getBookID() {
        return bookID;
    }


    public String getBookName() {
        return bookName;
    }

    

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }


    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }
    
    
    public String getLanguage() {
        return language;
    }


    public int getNumberofPage() {
        return numberOfPages;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    //getter
    public boolean isAvailable(){ 
        return isAvailable; 
    }

    //setter
    public void setAvailability(boolean available) {
        this.isAvailable = available;
    }

    

    
    public String generateNextBookID() {
        String alphabet = "BK";
        String number="0001";
        String BookID=null;
        int maxIdNumber = 0;
        if(BookFileManager.getBookList().size()==0){
            BookID=alphabet+number;
        }

        for(Book book:BookFileManager.getBookList()){
            String[] alphabet_number =book.getBookID().split("(?<=\\D)(?=\\d)");; // Split into alphabet and number
                
                alphabet = alphabet_number[0]; // Get the alphabet part
                number = alphabet_number[1];   // Get the numeric part

                
                int idNumber = Integer.parseInt(number); 
                maxIdNumber=Math.max(idNumber,maxIdNumber);
                
                
               
        }
        maxIdNumber++; 
                
            
        String idnum = String.format("%0" + number.length() + "d", maxIdNumber);  //%05d idNumber
        BookID=alphabet + idnum;
        
    

        
        return BookID;
    }
    
    


    public String toString() {
        return String.join("\t", bookID, bookName, author,isbn,genre,publisher, language, 
                           String.valueOf(numberOfPages), String.valueOf(publicationYear),isAvailable ? "Available" : "Unavailable");
    }

    public String toWord() {
        return bookID + bookName + author + isbn+genre + language +
                String.valueOf(numberOfPages) + String.valueOf(publicationYear)+ (isAvailable ? "Available" : "Unavailable");
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
    
        Book book = (Book) obj;
        return this.bookID.equalsIgnoreCase(book.bookID); // Compare by unique ID
    }

    

    @Override
    public int hashCode() {
        return Objects.hash(bookID); // Same fields as in equals()
    }

}