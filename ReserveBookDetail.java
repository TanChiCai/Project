
import java.time.LocalDate;


public class ReserveBookDetail extends BookTransactionDetail {
    private LocalDate reservationMadeDate, reservationBookDate;

    public ReserveBookDetail() {

    }
    public ReserveBookDetail(String reserveId, UserData user, Book book, LocalDate madeDate, LocalDate bookDate,
                             String status, String imageFile) {
        super(reserveId, user, book, status, imageFile);
        this.reservationMadeDate = madeDate;
        this.reservationBookDate = bookDate;
    }

    public LocalDate getReservationMadeDate() {
        return reservationMadeDate;
    }

    public LocalDate getReservationBookDate() {
        return reservationBookDate;
    }

    @Override
    public String getId() {
        return id;
    }
    public String generateNextId() {
        String alphabet = "RB";
        String number="0001";

        String bookReserveId=null;
        int maxidNumber = 0;
        if(BookFileManager.getReserveBookList().size()==0){
            bookReserveId=alphabet+number;
        }

        for(ReserveBookDetail reserveBook:BookFileManager.getReserveBookList()){
            String[] alphabet_number =reserveBook.getId().split("(?<=\\D)(?=\\d)");; // Split into alphabet and number
                
                alphabet = alphabet_number[0]; // Get the alphabet part
                number = alphabet_number[1];   // Get the numeric part

                
                int idNumber = Integer.parseInt(number); 
                maxidNumber=Math.max(idNumber,maxidNumber);
                
                
               
        }
        maxidNumber++; 
                
            
        String idnum = String.format("%0" + number.length() + "d", maxidNumber);  //%05d idNumber
        bookReserveId=alphabet + idnum;
        
    

        
        return bookReserveId;
    }

    @Override
    public String toString() {
        return String.join("\t",
            id, user.getUserid(), book.getBookID(),
            reservationMadeDate.format(FORMATTER),
            reservationBookDate.format(FORMATTER),
            status,
            imageFile);
    }

    @Override
    public String toWord() {
        return id + book.getBookName()+ user.getName()+user.getPhoneNumber()  +
            reservationMadeDate.format(FORMATTER) +
            reservationBookDate.format(FORMATTER) +
            status + imageFile;
    }
}
