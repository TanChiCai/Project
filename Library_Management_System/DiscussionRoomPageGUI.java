import javax.swing.*;
import java.awt.*;

public class DiscussionRoomPageGUI extends JPanel {
    public DiscussionRoomPageGUI(CardLayout cardLayout, JPanel cardPanel, UserData currentUser) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Wrapper Panel to hold both topPanel and imageTextPanel 
        JPanel topContainerDiscussion = new JPanel(new BorderLayout());
        topContainerDiscussion.setBackground(Color.WHITE);

        // Top Panel for Back Button and Title 
        JPanel topPanelDiscussion = new JPanel(new GridBagLayout());
        topPanelDiscussion.setBackground(Color.WHITE);
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
        JLabel roomLabel = new JLabel("Discussion Room", SwingConstants.CENTER);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 30));

        // === Positioning using GridBagLayout ===
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Back Button (Left-aligned)
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        topPanelDiscussion.add(backButton, gbc);

        // Title Label (Centered but pushed left)
        gbc.gridx = 1;
        gbc.weightx = 0.6; // Give it more space to balance alignment
        gbc.anchor = GridBagConstraints.CENTER;
        topPanelDiscussion.add(roomLabel, gbc);

        // === Panel to hold image and description side by side ===
        JPanel imageTextPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        imageTextPanel.setBackground(Color.WHITE);
        imageTextPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Add spacing

        GridBagConstraints gbcImageText = new GridBagConstraints();
        gbcImageText.insets = new Insets(10, 10, 10, 10); // Add spacing
        gbcImageText.anchor = GridBagConstraints.CENTER; // Center alignment

        // Load and Resize Discussion Room Image
        ImageIcon roomImageIcon = new ImageIcon("Image/DiscussionRoom.jpg");
        Image scaledRoomImage = roomImageIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledRoomImage));

        gbcImageText.gridx = 0;
        gbcImageText.gridy = 0;
        gbcImageText.gridwidth = 2; // Span both columns for proper centering
        imageTextPanel.add(imageLabel, gbcImageText);

        // Description Label using HTML for icons and line breaks
        JLabel detailsLabel = new JLabel(
                "<html><div style='text-align: center; width: 350px;'>" +
                "<span style='color: green;'>👥</span> MIN Capacity: 2 People<br>" +
                "<span style='color: red;'>👥</span> MAX Capacity: 5 People<br>" +
                "<span style='color: orange;'>⏳</span> MAX Duration: 2 hours<br>" +
                "<span style='color: purple;'>⌚</span> Available: 8AM - 9PM Every Day<br>" +
                "<span style='color: blue;'>📶</span> Free WiFi<br>" +
                "</div></html>"
        );
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        detailsLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the text

        gbcImageText.gridy = 1; // Place below the image
        gbcImageText.gridwidth = 3; // Center-align the text below the image
        imageTextPanel.add(detailsLabel, gbcImageText);

        // === Add both topPanel and imageTextPanel to the wrapper ===
        topContainerDiscussion.add(topPanelDiscussion, BorderLayout.NORTH);  // Keep title & back button at the very top
        topContainerDiscussion.add(imageTextPanel, BorderLayout.CENTER);  // Center the image & descriptions

        // Finally, add topContainer to NORTH to maintain proper positioning
        add(topContainerDiscussion, BorderLayout.NORTH);

        // === Reserve Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center button
        buttonPanel.setBackground(Color.WHITE);

        JButton reserveDiscussionButton = new JButton("Reserve Now");
        reserveDiscussionButton.setFont(new Font("Arial", Font.BOLD, 18));
        reserveDiscussionButton.setBackground(new Color(50, 150, 250)); // Blue color
        reserveDiscussionButton.setForeground(Color.WHITE);
        reserveDiscussionButton.setFocusPainted(false);
        reserveDiscussionButton.setPreferredSize(new Dimension(200, 40));
        reserveDiscussionButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reserveDiscussionButton.addActionListener(e -> cardLayout.show(cardPanel, "ReserveDiscussionRoom"));

        // Add button to panel
        buttonPanel.add(reserveDiscussionButton);

        // === Position Reserve Button Below Description ===
        gbcImageText.gridy = 2; // Below description
        gbcImageText.gridwidth = 3; // Center-align
        imageTextPanel.add(buttonPanel, gbcImageText);
    }
}