import java.time.LocalDate;

public class BorrowDetail extends BookTransactionDetail {
    private LocalDate borrowDate, dueDate, returnDate;
    private String borrowMethod;

    public BorrowDetail() {
        
    }

    public BorrowDetail(String borrowID, UserData user, Book book, LocalDate borrowDate, LocalDate returnDate,
                        String status, String method, String imageFile) {
        super(borrowID, user, book, status, imageFile);
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(6);
        this.returnDate = returnDate;
        this.borrowMethod = method;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBorrowMethod() {
        return borrowMethod;
    }

    public void setBorrowMethod(String borrowMethod) {
        this.borrowMethod = borrowMethod;
    }

    public String generateNextId() {
        String alphabet = "BR";
        String number="0001";
        String borrowId=null;
        int maxIdNumber = 0;
        

        for(BorrowDetail borrow:BookFileManager.getBorrowedList()){
            String[] alphabet_number =borrow.getId().split("(?<=\\D)(?=\\d)");; // Split into alphabet and number
                
                alphabet = alphabet_number[0]; // Get the alphabet part
                number = alphabet_number[1];   // Get the numeric part

                
                int idNumber = Integer.parseInt(number); 
                maxIdNumber=Math.max(idNumber,maxIdNumber);
                
                
               
        }
        maxIdNumber++; 
                
            
        String idnum = String.format("%0" + number.length() + "d", maxIdNumber);  //%05d idNumber
        borrowId=alphabet + idnum;
        return borrowId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.join("\t",
            id, user.getUserid(), book.getBookID(),
            borrowDate.format(FORMATTER),
            dueDate.format(FORMATTER),
            returnDate != null ? returnDate.format(FORMATTER) : "-",
            status,
            borrowMethod,
            imageFile);
    }

    @Override
    public String toWord() {
        return id + book.getBookName()+book.getBookID()+user.getName()+user.getPhoneNumber()  + borrowDate.format(FORMATTER) +
            dueDate.format(FORMATTER) + (returnDate != null ? returnDate.format(FORMATTER) : "-") +
            status + borrowMethod;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BorrowDetail other = (BorrowDetail) obj;
        return id.equalsIgnoreCase(other.id);
    }

    
}
