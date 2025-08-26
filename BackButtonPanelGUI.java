import javax.swing.*;
import java.awt.*;

public class BackButtonPanelGUI extends JPanel {
    private JButton backButton;  // Remove static

    public BackButtonPanelGUI() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));

        // Load Back Button Icon
        ImageIcon backImage = new ImageIcon("Image/BackButton.png");
        Image originalImage = backImage.getImage();
        Image scaledImage = originalImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        // Configure Back Button
        backButton = new JButton("Back", resizedIcon);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        backButton.setIconTextGap(5);
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(backButton); // Add button to the panel
    }

    public JButton getBackButton() {
        return backButton;
    }
}
