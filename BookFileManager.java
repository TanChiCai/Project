
import java.io.BufferedWriter;
import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;



import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;


public class BookFileManager extends FileManagement {
    private static final String BORROWFILE="borrowBook.txt";
    private static final String BOOKFILE="Book.txt";
    private static String BOOK_IMAG_FILE="book_covers";
    private static final String RESERVEBOOKFILE="reserveBook.txt";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
            
    // Getter for BORROWFILE
    public static String getBorrowFile() {
        return BORROWFILE;
    }

    // Getter for BOOKFILE
    public static String getBookImageFile() {
        return BOOK_IMAG_FILE;
    }

    // Getter for BOOKFILE
    public static String getBookFile() {
        return BOOKFILE;
    }

    public static String getReserveBookFile() {
        return RESERVEBOOKFILE;
    }



    private static List<Book> bookList = new ArrayList<>();
    private static List<BorrowDetail> borrowedList = new ArrayList<>();
    private static List<String> bookImageList = new ArrayList<>();
    private static List<ReserveBookDetail> reserveBookList = new ArrayList<>();



    public static List<Book> getBookList() {
        return bookList;
    }

    public static List<BorrowDetail> getBorrowedList() {
        return borrowedList;
    }

    public static List<String> getBookImageList() {
        return bookImageList;
    }

    public static List<ReserveBookDetail> getReserveBookList() {
        return reserveBookList;
    }
    public static Map<String, Book> loadBookMap() {
        Map<String, Book> bookMap = new HashMap<>();

        for (Book book : BookFileManager.getBookList()) {
            bookMap.put(book.getBookID(), book); // Assuming getBookID() returns the unique ID
        }

        return bookMap;
    }

    // To store book in one list from file
    public static void loadBooks(String filePath) {
        try (BufferedReader reader = getFileReader(filePath)) {
            reader.readLine(); // Skip header
            
            String line;
            while ((line = reader.readLine()) != null) {
                
                

                String[] data = line.split("\t");
                
                if(filePath.equals(BookFileManager.getReserveBookFile())){
                    String reserveBookId=data[0].strip();
                    String userId= data[1].strip();
                    String bookId= data[2].strip();
                    
                    Map<String, UserData> userMap = UserFileManager.loadUserMap();
                    UserData user = userMap.get(userId);

                    Map<String, Book> bookMap = loadBookMap();
                    Book book = bookMap.get(bookId);

                    String bookReservationMadeDateString = data[3].strip();
                    
                    LocalDate BookReservationMadeDate = LocalDate.parse(bookReservationMadeDateString, formatter);
                    
                    

                    String bookReservationBookDateString = data[4].strip();
                    LocalDate bookReservationBookDate = LocalDate.parse(bookReservationBookDateString, formatter);

                    String bookReservationStatus= data[5].strip();
                    
                    String imageFile=data[6].strip();

                    
                    ReserveBookDetail reserve=new ReserveBookDetail(reserveBookId,user,book,BookReservationMadeDate,bookReservationBookDate,bookReservationStatus,imageFile);
                    
                    reserveBookList.add(reserve);
     
                }
               
                
                if (filePath.equals(BookFileManager.getBookFile())){
                    String bookID = data[0].trim();
                    String bookName = data[1].trim();
                    String author = data[2].trim();
                    String isbn = data[3].trim();
                    String genre = data[4].trim();
                    String publisher = data[5].trim();
                    String language = data[6].trim();
                    int numberOfPages = Integer.parseInt(data[7].trim());
                    int publicationYear = Integer.parseInt(data[8].trim());
                    boolean isAvailable = data[9].trim().equalsIgnoreCase("Available");

                    
                    Book book =new Book(bookID,bookName,author,isbn,genre,publisher,language,numberOfPages,publicationYear,isAvailable);
                    bookList.add(book);
                }
                
                if (filePath.equals(BookFileManager.getBorrowFile())){

                    String borrowId= data[0].strip();
                    String userId= data[1].strip();
                    String bookId= data[2].strip();
                    Map<String, UserData> userMap = UserFileManager.loadUserMap();
                    UserData user = userMap.get(userId);

                    Map<String, Book> bookMap = loadBookMap();
                    Book book = bookMap.get(bookId);

                    String borrowDateString = data[3].strip();
                    
                    LocalDate borrowDate = LocalDate.parse(borrowDateString, formatter);

                    String returnDateString = data[5].strip();
                    LocalDate returnDate=null;
                    if (!returnDateString.equals("-")) {
                        returnDate = LocalDate.parse(returnDateString, formatter);
                    }
                    

                    String borrowBookStatus=data[6].strip();
                    String borrowMethod=data[7].strip();
                    String image=data[8].strip();
                    
                    BorrowDetail borrow = new BorrowDetail(borrowId,user,book, borrowDate,returnDate,borrowBookStatus,borrowMethod,image);
                    
                    
                    borrowedList.add(borrow);
          
                }

               
                
                
            }
        
        } catch (Exception e) {
            System.out.println("Error: File not found at path: " + e);
        }
        
             
        
    }

    


    public static void loadImagesFromFolder(String folderPath) {
        Set<Integer> numbers = new TreeSet<>();
    
        
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (folder.exists() && folder.isDirectory()) {
            // Filter the files and get only those that end with .jpg
        
            if (files != null && files.length > 0) {
                
                for (File file:files) {
                        String fileName = file.getName(); // ✅ Get the file name as a String
            
                        String[] parts = fileName.split("\\D+"); // ✅ Split by non-digit characters
            
                    for (String part : parts) {
                        if (!part.isEmpty()) {
                            numbers.add(Integer.parseInt(part));
                        }
                    }
                }
                for(Integer number:numbers){
                    
                    String arrangedFileName = "bookCover_"+number + ".jpg";
                    bookImageList.add(arrangedFileName);
                }
                
            } else {
                System.out.println("No .jpg files found in the folder.");
            }
        } else {
            System.out.println("The folder 'bookcover' does not exist or is not a directory.");
        }
        
        

    }


    public static void updateFile(String fileName) {
    
        File file = new File(fileName);
        
        try (BufferedWriter writer = getFileWriter(fileName, false)) { // 'true' to append
            // Check if the file is empty
            if (fileName.equalsIgnoreCase(BookFileManager.getBorrowFile())){
                if (file.length() == 0) {
                    // If the file is empty, write the header
                    writer.write("BorrowID\tUserID\tBookID\tBorrowDate\tDueDate\tReturnDate\tBorrowed Status\tBorrwed Method\tImage");
                    
                    writer.newLine();
                }
                    // Write each Borrow object to the file
                for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
                    
                    writer.write(borrow.toString());
                    writer.newLine(); // Add a new line after each borrow entry
                }
    
            }
            if(fileName.equalsIgnoreCase(BookFileManager.getReserveBookFile())){
                if (file.length() == 0) {
                    // If the file is empty, write the header
                    writer.write("ReserveBookId\tUser ID\tBook ID\tBookReservationMadeDate\tBookReservationBookDate\tBookReservationStatus");
                    writer.newLine();
                }
                    // Write each Borrow object to the file
                for (ReserveBookDetail reserveBook : BookFileManager.getReserveBookList()) {
                    
                    writer.write(reserveBook.toString());
                    writer.newLine(); // Add a new line after each borrow entry
                }
            }
            
            if(fileName.equalsIgnoreCase(BookFileManager.getBookFile())){
                if (file.length() == 0) {
                    // If the file is empty, write the header
                    writer.write("BookID\tBookName\tAuthor\tISBN\tGenre\tPublisher\tLanguage\tNumberofPage\tPublicationYear\tAvailability");
                    writer.newLine();
                }
                    // Write each Borrow object to the file
                for (Book book : BookFileManager.getBookList()) {
                    
                    writer.write(book.toString());
                    writer.newLine(); // Add a new line after each borrow entry
                }
            }
            
    
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    
    }
    
}