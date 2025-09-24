import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ReservationTableManager {
    private static final String RESERVATION_FILE = "Reservation.txt";
    private static final String PASSED_RESERVATION_FILE = "ReservationPassed.txt";
    private static final String USER_FILE = "users.txt";

    public DefaultTableModel getUserRoomReservationsTableModel(String userID) {
        String[] columnFields = {
            "Reservation ID", "Room", "Reservation Date", "Duration", "No. of Pax", "Equipments", "Status"
        };
        DefaultTableModel tableModel = new DefaultTableModel(columnFields, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // all cells are non-editable
            }
        };

        RoomManager roomManager = new RoomManager();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            reader.readLine(); // Skip header
            String line;
    
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9 && parts[1].equals(userID)) {
                    String reservationID = parts[0].trim();
                    String roomID = parts[2].trim();
                    String roomName = roomManager.getRoomNameByID(roomID); // no map used
                    String reservationDate = parts[3].trim();
                    String startTime = parts[4].trim();
                    String endTime = parts[5].trim();
                    String duration = startTime + " - " + endTime;
                    String noOfPax = parts[6].trim();
                    String equipment = parts[7].trim();
                    String status = parts[8].trim();
    
                    tableModel.addRow(new Object[]{
                        reservationID, roomName, reservationDate, duration, noOfPax, equipment.replace(";", ","), status
                    });
                }
            }
    
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return tableModel;
    }

    public static int[][] getMonthlyReservationByYear(int year) {
        int[][] summary = new int[2][13]; // 0 = Discussion, 1 = Conference; [0-11]=months, [12]=total

        try (BufferedReader reader = new BufferedReader(new FileReader("ReservationPassed.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 4) continue; // Skip invalid lines

                String roomID = parts[2].trim();
                String dateStr = parts[3].trim();

                try {
                    LocalDate date = LocalDate.parse(dateStr); // Format: yyyy-MM-dd

                    if (date.getYear() == year) {
                        int monthIndex = date.getMonthValue() - 1;

                        if (roomID.startsWith("RD")) {
                            summary[0][monthIndex]++;
                            summary[0][12]++; // total
                        } else if (roomID.startsWith("RC")) {
                            summary[1][monthIndex]++;
                            summary[1][12]++; // total
                        }
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format in line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return summary;
    }

    public static void loadReservationsByStatus(String filename, DefaultTableModel tableModel, String statusFilter) {
        tableModel.setRowCount(0);
    
        // Step 1: Load all user lines into a list (excluding header)
        List<String[]> userList = new ArrayList<>();
        try (BufferedReader userReader = new BufferedReader(new FileReader(USER_FILE))) {
            String userLine;
    
            while ((userLine = userReader.readLine()) != null) {
                String[] userFields = userLine.split("\t", -1);
                if (userFields.length == 4) {
                    userList.add(userFields); // [userID, username, password, contact]
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading user file: " + e.getMessage());
            return;
        }
    
        // Step 2: Load reservation data
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;
    
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
    
                String[] data = line.split(",", -1);
                if (data.length == 9 && data[8].equalsIgnoreCase(statusFilter)) {
                    String userID = data[1].trim();
                    String username = "Unknown";
                    String contact = "N/A";
    
                    // Look up user info
                    for (String[] user : userList) {
                        if (user[0].equals(userID)) {
                            username = user[1];
                            contact = user[3];
                            break;
                        }
                    }
    
                    String duration = data[4] + " - " + data[5];
                    String equipmentFormatted = data[7].replace(";", ",");
    
                    String[] row = {
                        data[0], // reservationID
                        data[1], // userID
                        username,
                        contact,
                        data[2], // roomID
                        data[3], // roomName
                        duration,
                        data[6], // purpose
                        equipmentFormatted,
                        data[8]  // status
                    };
    
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading reservation file: " + e.getMessage());
        }
    }

    public static void setupTable(JTable table, String statusType) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    
        // Center-align cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(240);
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    
        // Set up the correct combo box editor for the "Status" column (Default table view)
        JComboBox<String> statusComboBox;
        if (statusType.equals("Pending")) {
            statusComboBox = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
        } else {
            statusComboBox = new JComboBox<>(new String[]{"Confirmed", "Finished", "Cancelled"});
        }
    
        table.getColumnModel().getColumn(9).setCellEditor(new DefaultCellEditor(statusComboBox));
    }

    public static void updateStatusDropdownStaff(JTable table, String statusType) {
        // Get the editor for the status column
        TableColumn statusColumn = table.getColumnModel().getColumn(9); // Status column index
        JComboBox<String> statusComboBox;
        
        // Set the combo box model based on the table's current status (Switch table view)
        if (statusType.equals("Confirmed")) {
            statusComboBox = new JComboBox<>(new String[]{"Confirmed","Pending", "Finished", "Cancelled"});
        } else {
            statusComboBox = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
        }

        // Update the editor to use the new combo box
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));
    }

    public static void updateReservationStatus(DefaultTableModel tableModel, String type) {
        List<Reservation> updatedReservations = new ArrayList<>();
        List<Reservation> passedReservations = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            boolean isFirstLine = true;
            String headerLine = "";
    
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    headerLine = line;
                    isFirstLine = false;
                    continue;
                }
    
                String[] fields = line.split(",", -1); // handle empty fields
                String reservationID = fields[0];
                boolean updated = false;

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String tableResID = tableModel.getValueAt(i, 0).toString();
                    String updatedStatus = tableModel.getValueAt(i, 9).toString();
    
                    if (reservationID.equals(tableResID)) {
                        if (updatedStatus.equalsIgnoreCase("Cancelled")) {
                            updated = true;
                            break;
                        }
                        
                        Room room = RoomManager.createRoomByID(fields[2]);

                        Reservation res = new Reservation(
                            fields[0],
                            new UserData(fields[1]),
                            room,
                            LocalDate.parse(fields[3]),
                            LocalTime.parse(fields[4]),
                            LocalTime.parse(fields[5]),
                            Integer.parseInt(fields[6]),
                            fields[7]
                        );
                        res.setStatus(updatedStatus);
    
                        if (type.equals("Confirmed") && updatedStatus.equals("Finished")) {
                            passedReservations.add(res);
                        } else {
                            updatedReservations.add(res); // Still stays in Reservation.txt
                        }
    
                        updated = true;
                        break;
                    }
                }
    
                if (!updated) {
                    // Keep original line as a Reservation object
                    Room room = RoomManager.createRoomByID(fields[2]);

                    Reservation res = new Reservation(
                        fields[0],
                        new UserData(fields[1]),
                        room,
                        LocalDate.parse(fields[3]),
                        LocalTime.parse(fields[4]),
                        LocalTime.parse(fields[5]),
                        Integer.parseInt(fields[6]),
                        fields[7]
                    );
                    res.setStatus(fields[8]);
                    updatedReservations.add(res);
                }
            }
    
            // Overwrite Reservation.txt
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESERVATION_FILE))) {
                writer.write(headerLine);
                writer.newLine();
                for (Reservation updatedLine : updatedReservations) {
                    writer.write(updatedLine.toString());
                    writer.newLine();
                }
            }
    
            // Append finished records to ReservationPassed.txt
            if (!passedReservations.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSED_RESERVATION_FILE, true))) {
                    for (Reservation passed : passedReservations) {
                        writer.write(passed.toString());
                        writer.newLine();
                    }
                }
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
