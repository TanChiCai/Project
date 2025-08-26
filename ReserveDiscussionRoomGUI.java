import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class ReserveDiscussionRoomGUI extends JPanel{
    public ReserveDiscussionRoomGUI(CardLayout cardLayout, JPanel cardPanel, UserData currentUser) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === Back Button Panel (Top-Left) ===
        BackButtonPanelGUI backButtonPanel = new BackButtonPanelGUI();
        JButton backButton = backButtonPanel.getBackButton();  // Get button from instance
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "DiscussionRoom"));

        // === Main Panel for Form ===
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60)); // Padding

        // Title Panel
        JLabel titleLabel = new JLabel("Discussion Room Reservation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Remove spacing
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20)); // Adds padding around form
        formPanel.setPreferredSize(new Dimension(500, 250)); // Make the form larger

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 15, 20, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Increase font size for labels
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        // Date Selection (Changed to dd/MM/yyyy)
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");  // Set date format to dd/MM/yyyy
        dateChooser.setPreferredSize(new Dimension(150, 30));
        dateChooser.setEnabled(true);  // Ensure the calendar is enabled

        //Set min and max selectable dates
        Date currentDate = new Date(); // Today
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Set minimum selectable date to today
        dateChooser.setMinSelectableDate(currentDate);

        // Set maximum selectable date to 7 days from today
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date maxDate = calendar.getTime();
        dateChooser.setMaxSelectableDate(maxDate);

        // Access the text field inside JDateChooser
        JTextField dateTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateTextField.setText("dd/mm/yyyy");
        dateTextField.setEditable(false);  // Disable manual input

        // Make the input field with a custom background and border
        dateTextField.setForeground(Color.BLACK);  // Black text color
        dateTextField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));  // Standard gray border
        dateTextField.setFont(new Font("Arial", Font.PLAIN, 14));  // Standard font

        // Time Selection
        String[] times = {"08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM", "07:00 PM", "08:00 PM", "09:00 PM"};
        JComboBox<String> startTimeBox = new JComboBox<>(times);
        startTimeBox.setPreferredSize(new Dimension(150, 30));
        JComboBox<String> endTimeBox = new JComboBox<>(times);
        endTimeBox.setPreferredSize(new Dimension(150, 30));

        // Pax Selection
        String[] paxOptions = {"2", "3", "4", "5"};
        JComboBox<String> paxBox = new JComboBox<>(paxOptions);
        paxBox.setPreferredSize(new Dimension(80, 30));

        // Labels and Adding Components
        JLabel dateLabel = new JLabel("Reservation Date: ");
        dateLabel.setFont(labelFont);
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(dateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;

        JLabel startLabel = new JLabel("Start Time: ");
        startLabel.setFont(labelFont);
        formPanel.add(startLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(startTimeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;

        JLabel endLabel = new JLabel("End Time: ");
        endLabel.setFont(labelFont);
        formPanel.add(endLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(endTimeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;

        JLabel paxLabel = new JLabel("Number of People: ");
        paxLabel.setFont(labelFont);
        formPanel.add(paxLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(paxBox, gbc);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setBackground(new Color(0, 153, 76)); // Green color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(e -> {
            // Get the current date and time
            String dateFieldValue = dateTextField.getText();

            // If the user hasn’t picked a date (still placeholder text)
            if (dateFieldValue.equalsIgnoreCase("dd/mm/yyyy")) {
                JOptionPane.showMessageDialog(this, "Please select a date from the calendar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDateRaw = dateChooser.getDate();

            int startIndex = startTimeBox.getSelectedIndex();
            int endIndex = endTimeBox.getSelectedIndex();
            int noOfPax = Integer.parseInt((String) paxBox.getSelectedItem());
            
            /// Parse selected start time to hour and minute
            String selectedTime = (String) startTimeBox.getSelectedItem();
            String selectedEndTimeStr = (String) endTimeBox.getSelectedItem();

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime selectedStartTime = LocalTime.parse(selectedTime, timeFormatter);
            LocalTime selectedEndTime = LocalTime.parse(selectedEndTimeStr, timeFormatter);

            // Construct the selected LocalDateTime
            LocalDate selectedDate = selectedDateRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Validate using the new validator
            String error = new ValidateReservation().validateDiscussionRoom(startIndex, endIndex, selectedDate, selectedStartTime, currentUser, "Reservation.txt");
            
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // === Confirmation Dialog ===
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to confirm your reservation?",
                    "Confirm Reservation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Find an available discussion room
                RoomManager roomManager = new RoomManager();
                ReservationManager reservationManager = new ReservationManager();

                // Get the first available discussion room
                DiscussionRoom availableRoom = roomManager.findAvailableDiscussionRoom(selectedDate, selectedStartTime, selectedEndTime, reservationManager);

                if (availableRoom == null) {
                    JOptionPane.showMessageDialog(this, "No available discussion rooms!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate a unique reservation ID
                String reservationID = "R" + System.currentTimeMillis();

                // Retrieve equipment info (will be empty for discussion room, but preserved for logic)
                String equipmentInfo = availableRoom.getEquipmentString();

                // Create the reservation
                Reservation reservation = new Reservation(reservationID, currentUser, availableRoom, selectedDate, selectedStartTime, selectedEndTime, noOfPax, equipmentInfo);

                // Save the reservation using ReservationManager
                reservationManager.saveUserReservation(reservation);

                JOptionPane.showMessageDialog(this, "Reservation Confirmed!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Set the flag to indicate reservation was made
                currentUser.setReservationMade(true);
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        // Add Components to Main Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        add(backButtonPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
