import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

public class ReservationManager {
    private static final String RESERVATIONS_FILE = "Reservation.txt";
    
    public boolean isTimeSlotAvailable(String roomID, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;

                try {
                    String existingRoomID = parts[2].trim();
                    LocalDate existingDate = LocalDate.parse(parts[3].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalTime existingStart = LocalTime.parse(parts[4].trim(), DateTimeFormatter.ofPattern("HH:mm"));
                    LocalTime existingEnd = LocalTime.parse(parts[5].trim(), DateTimeFormatter.ofPattern("HH:mm"));

                    if (existingRoomID.equals(roomID) && existingDate.equals(date)) {
                        // Check for any overlap
                        if (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)) {
                            return false;
                        }
                    }
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading reservations file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    public void saveUserReservation(Reservation reservation) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATIONS_FILE, true))) {
            writer.write(reservation.getReservationID() + "," + 
                         reservation.getUser().getUserid() + "," + 
                         reservation.getRoom().getRoomID() + "," + 
                         reservation.getReservationDate() + "," + 
                         reservation.getStartTime() + "," + 
                         reservation.getEndTime() + "," + 
                         reservation.getNoOfPax() + "," + 
                         reservation.getEquipments() + "," +
                         reservation.getStatus());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving reservation!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
