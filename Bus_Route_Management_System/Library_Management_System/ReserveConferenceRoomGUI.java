import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import java.util.Date;


public class ReserveConferenceRoomGUI extends JPanel{
    public ReserveConferenceRoomGUI (CardLayout cardLayout, JPanel cardPanel, UserData currentUser){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // Back Button Panel (Top-Left)
        // Set action for the static back button
        BackButtonPanelGUI backButtonPanel = new BackButtonPanelGUI();
        JButton backButton = backButtonPanel.getBackButton();  // Get button from instance
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "ConferenceRoom"));

        // Create a panel to hold the back button and add it to the UI
        add(backButtonPanel, BorderLayout.NORTH);

        // Main Panel for Form 
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding 
        
        // === Title Panel ===
        JLabel titleLabel = new JLabel("Conference Room Reservation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Remove spacing
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titleLabel);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20)); // Adds padding around form
        formPanel.setPreferredSize(new Dimension(400, 100)); // Make the form larger

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
        calendar.add(Calendar.DAY_OF_MONTH, 6);
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
        JTextField paxField = new JTextField();
        paxField.setPreferredSize(new Dimension(80, 30));
        paxField.setForeground(Color.GRAY);
        paxField.setText("E.g. 25"); // Placeholder text

        // Add focus listener to remove placeholder when clicked
        paxField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (paxField.getText().equals("E.g. 25")) {
                    paxField.setText("");
                    paxField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (paxField.getText().trim().isEmpty()) {
                    paxField.setText("E.g. 25");
                    paxField.setForeground(Color.GRAY);
                }
            }
        });

        // Equipments
        JCheckBox projectorCheckBox = new JCheckBox("Projector");
        JCheckBox whiteboardCheckBox = new JCheckBox("Whiteboard");
        JCheckBox microphoneCheckBox = new JCheckBox("Microphone");

        // Ensure background color matches the panel
        Font checkboxFont = new Font("Arial", Font.PLAIN, 14);
        projectorCheckBox.setFont(checkboxFont);
        whiteboardCheckBox.setFont(checkboxFont);
        microphoneCheckBox.setFont(checkboxFont);

        // Set white background for checkboxes
        projectorCheckBox.setBackground(Color.WHITE);
        whiteboardCheckBox.setBackground(Color.WHITE);
        microphoneCheckBox.setBackground(Color.WHITE);

        // Remove the black rectangular focus border
        projectorCheckBox.setFocusPainted(false);
        whiteboardCheckBox.setFocusPainted(false);
        microphoneCheckBox.setFocusPainted(false);

        // Add padding for better alignment
        projectorCheckBox.setMargin(new Insets(5, 5, 5, 15));
        whiteboardCheckBox.setMargin(new Insets(5, 5, 5, 15));
        microphoneCheckBox.setMargin(new Insets(5, 5, 5, 15));

        // Panel to hold checkboxes in a horizontal layout
        JPanel equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new GridLayout(1, 3, 20, 0)); // 20px space between checkboxes
        equipmentPanel.setBackground(Color.WHITE);
        equipmentPanel.add(projectorCheckBox);
        equipmentPanel.add(whiteboardCheckBox);
        equipmentPanel.add(microphoneCheckBox);

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
        formPanel.add(paxField, gbc);

        JLabel equipmentLabel = new JLabel("Equipments: ");
        equipmentLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 4; // Adjusted row position
        formPanel.add(equipmentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3; // Span across multiple columns
        formPanel.add(equipmentPanel, gbc);
        
        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setBackground(new Color(0, 153, 76)); // Green color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        add(mainPanel, BorderLayout.CENTER); // Add the main panel

        submitButton.addActionListener(e -> {
            // Get the current date and time
            String dateFieldValue = dateTextField.getText();

            // If the user hasn’t picked a date (still placeholder text)
            if (dateFieldValue.equalsIgnoreCase("dd/mm/yyyy")) {
                JOptionPane.showMessageDialog(this, "Please select a date from the calendar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date selectedDateRaw = dateChooser.getDate();
            
            // Get selected time indexes
            int startIndex = startTimeBox.getSelectedIndex();
            int endIndex = endTimeBox.getSelectedIndex();
            
            // Get pax input
            String paxInput = paxField.getText();
            
            // Parse selected times
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            
            String startTimeStr = (String) startTimeBox.getSelectedItem();
            LocalTime selectedStartTime = LocalTime.parse(startTimeStr, timeFormatter);
            
            String endTimeStr = (String) endTimeBox.getSelectedItem();
            LocalTime selectedEndTime = LocalTime.parse(endTimeStr, timeFormatter);
            
            // Construct the selected LocalDateTime
            LocalDate selectedDate = selectedDateRaw.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Collect selected equipment
            ConferenceRoom tempRoom = new ConferenceRoom();
            
            if (projectorCheckBox.isSelected()) {
                tempRoom.addEquipment("Projector");
            }
            if (whiteboardCheckBox.isSelected()) {
                tempRoom.addEquipment("Whiteboard");
            }
            if (microphoneCheckBox.isSelected()) {
                tempRoom.addEquipment("Microphone");
            }

            String equipmentInfo = tempRoom.getEquipmentString();
            
            // Validate using validator
            ValidateReservation validator = new ValidateReservation();
            String error = validator.validateConferenceRoom(startIndex, endIndex, paxInput, selectedDate, selectedStartTime, currentUser, "Reservation.txt");
            
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Only reaches here if all validations passed
            int noOfPax = Integer.parseInt(paxInput.trim());

            // === Confirmation Dialog ===
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to confirm your reservation?",
                    "Confirm Reservation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Find an available conference room
                RoomManager roomManager = new RoomManager();
                ReservationManager reservationManager = new ReservationManager();

                // Get available study rooms using the new OOP approach
                ConferenceRoom selectedRoom = roomManager.findAvailableConferenceRoom(selectedDate, selectedStartTime, selectedEndTime, reservationManager);
                if (selectedRoom == null) {
                    JOptionPane.showMessageDialog(this, "No available conference rooms!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Generate a unique reservation ID
                String reservationID = "R" + System.currentTimeMillis();

                // Create and save reservation
                Reservation reservation = new Reservation(reservationID, currentUser, selectedRoom, selectedDate, selectedStartTime, selectedEndTime, noOfPax, equipmentInfo );
                reservationManager.saveUserReservation(reservation);

                JOptionPane.showMessageDialog(this, "Reservation Confirmed!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Set the flag to indicate reservation was made
                currentUser.setReservationMade(true);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(submitButton, gbc);

        // Add Components to Main Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(titlePanel, BorderLayout.NORTH);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    } 

}