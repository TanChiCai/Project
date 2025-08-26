import java.util.ArrayList;
import java.util.List;

public class ProcessFineManagement {
    
    public List<ProcessFineDetail> getAllLoans() {
        return this.processfine;  // or however you're storing them
    }

    private List<ProcessFineDetail> processfine;

    public ProcessFineManagement() {
        processfine = new ArrayList<>();
    }

    public void addLoan(ProcessFineDetail fine) {
        processfine.add(fine);
    }

    public void printAllLoans() {
        for (ProcessFineDetail fine : processfine) { 
            System.out.println(fine);
        }
    }
}
