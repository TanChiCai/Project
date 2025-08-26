import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProcessFineDetail {
	private String ProcessFindID;
	private UserData user;
	private Book book ;
	private int payment;
	private LocalDate payDate=null, fineDueDate=null;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean payStatus;

    public ProcessFineDetail(String ProcessFindID, UserData user, Book book, int payment, LocalDate payDate, LocalDate fineDueDate, boolean payStatus) {
    	this.ProcessFindID=ProcessFindID;
    	this.user=user;
    	this.book=book;
    	this.payment=payment;
    	this.payDate=payDate;
    	this.fineDueDate=fineDueDate;
    	this.payStatus=payStatus;
    }
    
    public ProcessFineDetail(){
    }

	public void setProcessFindID(String ProcessFindID){
		this.ProcessFindID=ProcessFindID;
	}
    
    public String getProcessFindID(){
    	return ProcessFindID;
    }
    
    public UserData getUser(){
        return user;
    }
    
    public Book getbook(){
    	return book;
    }

	public void setPayment(int payment){
		this.payment=payment;
	}
    
    public int getPayment(){
    	return payment;
    }
    
    public void setPayDate(LocalDate payDate) {
	    this.payDate = payDate;
	}
    
    public LocalDate getPayDate(){
    	return payDate;
    }

	public String getFormattedPayDate() {
		return (payDate == null) ? "-" : payDate.format(FORMATTER);
	}	
    
	public void setFineDueDate(LocalDate fineDueDate) {
	    this.fineDueDate = fineDueDate;
	}

    public LocalDate getFineDueDate(){
    	return fineDueDate;
    }
    
    public boolean isPayStatus(){
    	return payStatus;
    }
    
    public void setPayStatus(boolean payStatus){
    	this.payStatus=payStatus;
    }
    
    public String toString() {
	    String payDateStr = getFormattedPayDate();
	
	    return ProcessFindID + "\t"
	         + user.getUserid() + "\t"
			 + book.getBookID() + "\t"
	         + book.getBookName() + "\t"
	         + payment + "\t"
	         + payDateStr + "\t"
	         + fineDueDate + "\t"
	         + payStatus;
	}
}