import java.io.*;
import java.util.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class RoomManager {
    private static final String ROOMS_FILE = "Rooms.txt";

    public DiscussionRoom findAvailableDiscussionRoom(LocalDate date, LocalTime startTime, LocalTime endTime, ReservationManager reservationManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String roomID = parts[0].trim();
                    String roomName = parts[1].trim();
                    int capacity = Integer.parseInt(parts[2].trim());
    
                    if (roomID.startsWith("RD")) { // Only Discussion Rooms
                        DiscussionRoom room = new DiscussionRoom(roomID, roomName, capacity);
                        if (reservationManager.isTimeSlotAvailable(room.getRoomID(), date, startTime, endTime)) {
                            return room; // Return the first available discussion room
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading rooms file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        return null; // No available room found
    }

    // Get available Conference Rooms
    public ConferenceRoom findAvailableConferenceRoom(LocalDate date, LocalTime startTime, LocalTime endTime, ReservationManager reservationManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String roomID = parts[0].trim();
                    String roomName = parts[1].trim();
                    int capacity = Integer.parseInt(parts[2].trim());
    
                    if (roomID.startsWith("RC")) { // Only Conference Rooms
                        ConferenceRoom room = new ConferenceRoom(roomID, roomName, capacity, new ArrayList<>());
                        if (reservationManager.isTimeSlotAvailable(room.getRoomID(), date, startTime, endTime)) {
                            return room; // Return the first available conference room
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading rooms file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        return null; // No available room found
    }

    public String getRoomNameByID(String roomID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ROOMS_FILE))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].trim().equals(roomID)) {
                    return parts[1]; // Return roomName
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown"; // Fallback if not found
    }

    public static Room createRoomByID(String roomID) {
        try (BufferedReader roomReader = new BufferedReader(new FileReader(ROOMS_FILE))) {
            String roomLine;
            roomReader.readLine();
            while ((roomLine = roomReader.readLine()) != null) {
                String[] roomParts = roomLine.split(",", -1); // roomID,roomName,maxCapacity
                if (roomParts[0].equals(roomID)) {
                    String roomName = roomParts[1];

                    if (roomName.equalsIgnoreCase("Discussion Room")) {
                        return new DiscussionRoom(roomID);
                    } 
                    else if (roomName.equalsIgnoreCase("Conference Room")) {
                        return new ConferenceRoom(roomID);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null; // Room not found
    }
}

