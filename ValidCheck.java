import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ValidCheck {
    public static void main(String[] args) {
        try{
            
            List<ReserveBookDetail> reserveBookList = BookFileManager.getReserveBookList();

            if (reserveBookList == null) {
                System.out.println("Error: Borrowed list is null");
                return;
            }
            if (reserveBookList.isEmpty()) {
                System.out.println("No borrowed books found.");
                System.out.println("Possible reasons:");
                System.out.println("1. The borrow file is empty");
                System.out.println("2. The file format doesn't match expected format");
                System.out.println("3. There was an error parsing the file");
                return;
            }

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            for(ReserveBookDetail reserve : reserveBookList){
                System.out.println(reserve.getId());
            }
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Load the borrow file
            String borrowFilePath = BookFileManager.getBorrowFile();
            System.out.println("Loading borrow file from: " + borrowFilePath);
            
            BookFileManager.loadBooks(borrowFilePath);

            // Retrieve list
            List<BorrowDetail> borrowedList = BookFileManager.getBorrowedList();

            // Check if list is not null
            if (borrowedList == null) {
                System.out.println("Error: Borrowed list is null");
                return;
            }
            
            if (borrowedList.isEmpty()) {
                System.out.println("No borrowed books found.");
                System.out.println("Possible reasons:");
                System.out.println("1. The borrow file is empty");
                System.out.println("2. The file format doesn't match expected format");
                System.out.println("3. There was an error parsing the file");
                return;
            }

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Update overdue statuses
            boolean updated = false;
            for (BorrowDetail borrow : borrowedList) {
                LocalDate dueDate = borrow.getDueDate();
                if (borrow.getReturnDate() == null && today.isAfter(dueDate)) {
                    borrow.setStatus("Overdue");
                    updated = true;
                }
            }

            // Save updated list back to borrow file
            if (updated) {
                FileWriter writer = new FileWriter(BookFileManager.getBorrowFile(), false); // overwrite
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                for (BorrowDetail borrow : borrowedList) {
                    // Customize this format to match your actual file format
                    String line = String.join("\t",
                        borrow.getId(),
                        borrow.getUser().toString(),
                        borrow.getBook().toString(),
                        borrow.getBorrowDate().format(formatter),
                        borrow.getDueDate().format(formatter),
                        (borrow.getReturnDate() != null ? borrow.getReturnDate().format(formatter) : "-"),
                        borrow.getStatus()
                    );

                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
            }

        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}