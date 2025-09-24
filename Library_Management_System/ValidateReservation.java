import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;

public class ValidateReservation {
    // Study Room Validation
    public String validateDiscussionRoom(int startIndex, int endIndex, LocalDate selectedDate, LocalTime selectedStartTime, UserData currentUser, String reservationFilePath) {

        // 1. Time order validation (using indexes like original)
        if (startIndex >= endIndex) {
            return "End time must be after start time!";
        }
        
        // 2. Duration validation (2 hour max using indexes)
        if (endIndex - startIndex > 2) {
            return "Maximum reservation time is 2 hours!";
        }

        // 3. Date and time validations
        LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedStartTime);
        LocalDateTime nowPlusTwoHours = LocalDateTime.now().plusHours(2);
        if (selectedDateTime.isBefore(nowPlusTwoHours)) {
            return "You can only reserve a room at least 2 hours in advance!";
        }

        int totalReservations = 0;
        int totalHours = 0;
        LocalTime selectedEndTime = selectedStartTime.plusHours(endIndex - startIndex);

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFilePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 9) continue;

                String userID = parts[1];
                String roomID = parts[2];
                LocalDate date = LocalDate.parse(parts[3]);
                LocalTime startTime = LocalTime.parse(parts[4]);
                LocalTime endTime = LocalTime.parse(parts[5]);

                // Only check confirmed reservations of this user on selected date
                if (userID.equals(currentUser.getUserid()) && date.equals(selectedDate)) {
                    totalReservations++;

                    // Check for time overlap
                    if (selectedStartTime.isBefore(endTime) && selectedEndTime.isAfter(startTime)) {
                        return "You already have a reservation that overlaps this time!";
                    }

                    // Add to total hours
                    totalHours += Duration.between(startTime, endTime).toHours();
                }
            }
        } catch (IOException e) {
            return "Error reading reservation data.";
        }

        // New validation: Max 4 hours per day
        if (totalHours + (endIndex - startIndex) > 4) {
            return "You can only reserve up to 4 hours per day!";
        }

        // New validation: Max 2 reservations per day
        if (totalReservations >= 2) {
            return "You can only make 2 reservations per day!";
        }

        return null; // Valid
    }

    // Conference Room Validation 
    public String validateConferenceRoom(int startIndex, int endIndex, String paxInput, LocalDate selectedDate, LocalTime selectedStartTime, UserData currentUser, String reservationFilePath) {
        // 1. Validate number format first
        if (paxInput == null || paxInput.trim().isEmpty()) {
            return "Please enter number of people";
        }
        
        int noOfPax;
        try {
            noOfPax = Integer.parseInt(paxInput.trim());
        } catch (NumberFormatException ex) {
            return "Please enter a valid number in text field!";
        }
        
        // 2. Time order validation
        if (startIndex >= endIndex) {
            return "End time must be after start time!";
        }
        
        // 3. Duration validation (3 hours)
        if (endIndex - startIndex > 3) {
            return "Maximum reservation time is 3 hours!";
        }
        
        // 4. Pax validation
        if (noOfPax <= 9) {
            return "Number of people must be at least 10!";
        }
        if (noOfPax > 30) {
            return "Maximum capacity only 30 people!";
        }

        // 3. Date and time validations
        LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedStartTime);
        LocalDateTime nowPlusTwoHours = LocalDateTime.now().plusHours(2);
        if (selectedDateTime.isBefore(nowPlusTwoHours)) {
            return "You can only reserve a room at least 2 hours in advance!";
        }
        
        int totalReservations = 0;
        int totalHours = 0;
        LocalTime selectedEndTime = selectedStartTime.plusHours(endIndex - startIndex);

        try (BufferedReader reader = new BufferedReader(new FileReader(reservationFilePath))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 9) continue;

                String userID = parts[1];
                LocalDate date = LocalDate.parse(parts[3]);
                LocalTime startTime = LocalTime.parse(parts[4]);
                LocalTime endTime = LocalTime.parse(parts[5]);

                if (userID.equals(currentUser.getUserid()) && date.equals(selectedDate)) {
                    totalReservations++;

                    if (selectedStartTime.isBefore(endTime) && selectedEndTime.isAfter(startTime)) {
                        return "You already have a reservation that overlaps this time!";
                    }

                    totalHours += Duration.between(startTime, endTime).toHours();
                }
            }
        } catch (IOException e) {
            return "Error reading reservation data.";
        }

        if (totalHours + (endIndex - startIndex) > 4) {
            return "You can only reserve up to 4 hours per day!";
        }

        if (totalReservations >= 2) {
            return "You can only make 2 reservations per day!";
        }

        return null; // Valid
    }
}

