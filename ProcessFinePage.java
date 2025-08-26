import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

public class ProcessFinePage {

    public static void showFinePage(String userIdText, String bookIdText, boolean payStatus, int payment) {
        // Ensure book list is loaded before using
        if (BookFileManager.getBookList().isEmpty()) {
            BookFileManager.loadBooks(BookFileManager.getBookFile()); // Load from Book.txt
        }

        // Load book map for fast lookup
        Map<String, Book> bookMap = BookFileManager.loadBookMap();
        Book book = bookMap.get(bookIdText);

        if (book == null) {
            JOptionPane.showMessageDialog(null, "❌ Book ID not found in the system!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user (assuming only ID is known)
        UserData user = new UserData(userIdText);

        // Generate next ProcessFineID from file rows
        String processFineID = generateProcessFindID();

        LocalDate issueDate = LocalDate.now();
        LocalDate returnDate = payStatus ? issueDate : null;
        LocalDate dueDate = issueDate.plusDays(14);

        ProcessFineDetail newLoan = new ProcessFineDetail(
            processFineID, user, book, payment, returnDate, dueDate, payStatus
        );
        appendFineToFile(newLoan);
    }

    private static void appendFineToFile(ProcessFineDetail newLoan) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("processfine.txt", true))) {
            // Manually write each field separated by \t
            String line =
                newLoan.getProcessFindID() + "\t" +
                newLoan.getUser().getUserid() + "\t" +
                newLoan.getbook().getBookID() + "\t" +
                newLoan.getbook().getBookName() + "\t" +
                newLoan.getPayment() + "\t" +
                newLoan.getFormattedPayDate() + "\t" +
                newLoan.getFineDueDate() + "\t" +
                newLoan.isPayStatus();

            writer.write(line);   // Write the formatted string to file
            writer.newLine();      // Add a new line for the next entry
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "⚠️ Error saving fine record.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to generate processFineID based on the row number in the file
    public static String generateProcessFindID() {
        int rowCount = getRowCount();
        int nextNumber = rowCount + 1; // The next number will be one greater than the current row count
        return String.format("PF%04d", nextNumber);  // PF0001, PF0002, etc.
    }

    // Method to get the number of rows in the processfine.txt file
    private static int getRowCount() {
        int rowCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("processfine.txt"))) {
            while (reader.readLine() != null) {
                rowCount++; // Increment rowCount for each line
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "⚠️ Error reading file to get row count.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return rowCount;
    }
}