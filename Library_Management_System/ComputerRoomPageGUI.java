import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.*;
public class ComputerRoomPageGUI extends JPanel {
    public ComputerRoomPageGUI(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel roomLabel = new JLabel("Computer Room Details", SwingConstants.CENTER);
        roomLabel.setFont(new Font("Arial", Font.BOLD, 24));
        roomLabel.setBounds(300, 200, 300, 50);

        JLabel detailsLabel = new JLabel("<html>🖥️ 20 Computers Available<br>💾 Software: Python, Java, C++<br>⌚ 24/7 Access</html>", SwingConstants.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        detailsLabel.setBounds(250, 270, 400, 100);

        // Load and Resize Image
        ImageIcon backImage = new ImageIcon("src/BackButton.png"); // Load image
        Image originalImage = backImage.getImage(); // Get Image object
        Image scaledImage = originalImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // Resize to 20x20
        ImageIcon resizedIcon = new ImageIcon(scaledImage); // Create new ImageIcon

        JButton backButton = new JButton("Back", resizedIcon);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBounds(10, 10, 100, 30);
        backButton.setFocusPainted(false);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Text next to icon
        backButton.setIconTextGap(5); // Add spacing between icon and text
        backButton.setBackground(Color.LIGHT_GRAY);

        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Home"));

        add(roomLabel);
        add(detailsLabel);
        add(backButton);
    }
}

