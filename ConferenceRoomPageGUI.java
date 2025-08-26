import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.*;

public class ConferenceRoomPageGUI extends JPanel {
    public ConferenceRoomPageGUI(CardLayout cardLayout, JPanel cardPanel, UserData currentUser) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Wrapper Panel to hold both topPanel and imageTextPanel
        JPanel topContainerConference = new JPanel(new BorderLayout());
        topContainerConference.setBackground(Color.WHITE);

        // Top Panel for Back Button and Title
        JPanel topPanelConference = new JPanel(new GridBagLayout());
        topPanelConference.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        // Back Button
        BackButtonPanelGUI backButtonPanel = new BackButtonPanelGUI();
        JButton backButton = backButtonPanel.getBackButton();  
        backButton.addActionListener(e -> {
            // Navigate to the homepage
            cardLayout.show(cardPanel, "Home");

            // Check if reservation was made
            if (currentUser.getReservationMade()) {
                // Show the message when navigating back to the homepage
                JOptionPane.showMessageDialog(
                    this,
                    "Please exit and relaunch the reservation section to view your updated reservations in a table.",
                    "Refresh Required",
                    JOptionPane.INFORMATION_MESSAGE
                );
        
                // Reset the reservationMade flag
                currentUser.setReservationMade(false);  // Reset flag after showing message
            }
        });

        // Title Label (Centered but shifted left slightly)
        JLabel roomLabel = new JLabel("Conference Room", SwingConstants.CENTER);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 30));

         // === Positioning using GridBagLayout ===
         gbc.insets = new Insets(10, 10, 10, 10); // Padding

         // Back Button (Left-aligned)
         gbc.gridx = 1;
         gbc.anchor = GridBagConstraints.WEST;
         topPanelConference.add(backButton, gbc);
 
         // Title Label (Centered but pushed left)
         gbc.gridx = 1;
         gbc.weightx = 0.6; // Give it more space to balance alignment
         gbc.anchor = GridBagConstraints.CENTER;
         topPanelConference.add(roomLabel, gbc);

         // Panel to hold image and description side by side
        JPanel imageTextPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        imageTextPanel.setBackground(Color.WHITE);
        imageTextPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Add spacing

        GridBagConstraints gbcImageText = new GridBagConstraints();
        gbcImageText.insets = new Insets(10, 10, 10, 10); // Add spacing
        gbcImageText.anchor = GridBagConstraints.CENTER; // Center alignment

        // Load and Resize Study Room Image
        ImageIcon roomImageIcon = new ImageIcon("Image/ConferenceRoom.jpg");
        Image scaledRoomImage = roomImageIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledRoomImage));

        gbcImageText.gridx = 0;
        gbcImageText.gridy = 0;
        gbcImageText.gridwidth = 2; // Span both columns for proper centering
        imageTextPanel.add(imageLabel, gbcImageText);

        // Description Label using HTML for icons and line breaks
        JLabel detailsLabel = new JLabel(
                "<html><div style='text-align: center; width: 400px;'>" +
                "<span style='color: green;'>👥</span> MIN Capacity: 10 People<br>" +
                "<span style='color: red;'>👥</span> MAX Capacity: 30 People<br>" +
                "<span style='color: orange;'>⏳</span> MAX Duration: 3 hours<br>" +
                "<span style='color: purple;'>⌚</span> Available: 8AM - 9PM Every Day<br>" +
                "🎤 Projector, Whiteboard & Microphone<br>" +
                "<span style='color: blue;'>📶</span> Free WiFi<br>" +
                "</div></html>"
        );
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
    

        gbcImageText.gridy = 1; // Place below the image
        gbcImageText.gridwidth = 3; // Center-align the text below the image
        imageTextPanel.add(detailsLabel, gbcImageText);

        // === Add both topPanel and imageTextPanel to the wrapper ===
        topContainerConference.add(topPanelConference, BorderLayout.NORTH);  // Keep title & back button at the very top
        topContainerConference.add(imageTextPanel, BorderLayout.CENTER);  // Center the image & descriptions

        // Finally, add topContainer to NORTH to maintain proper positioning
        add(topContainerConference, BorderLayout.NORTH);

        // === Reserve Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center button
        buttonPanel.setBackground(Color.WHITE);

        JButton reserveConferenceButton = new JButton("Reserve Now");
        reserveConferenceButton.setFont(new Font("Arial", Font.BOLD, 18));
        reserveConferenceButton.setBackground(new Color(50, 150, 250)); // Blue color
        reserveConferenceButton.setForeground(Color.WHITE);
        reserveConferenceButton.setFocusPainted(false);
        reserveConferenceButton.setPreferredSize(new Dimension(200, 40));
        reserveConferenceButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reserveConferenceButton.addActionListener(e -> cardLayout.show(cardPanel, "ReserveConferenceRoom"));

        // Add button to panel
        buttonPanel.add(reserveConferenceButton);

        // === Position Reserve Button Below Description ===
        gbcImageText.gridy = 2; // Below description
        gbcImageText.gridwidth = 3; // Center-align
        gbcImageText.insets = new Insets(0, 0, 0, 0);
        imageTextPanel.add(buttonPanel, gbcImageText);
    }
}

