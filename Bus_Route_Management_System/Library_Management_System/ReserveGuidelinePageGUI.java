import javax.swing.*;
import java.awt.*;

public class ReserveGuidelinePageGUI extends JPanel{
    public ReserveGuidelinePageGUI(CardLayout cardLayout, JPanel cardPanel) {
        setLayout(null);
        setBackground(Color.WHITE);

        // Top Panel for Back Button and Title 
        JPanel topPanelDiscussion = new JPanel(new GridBagLayout());
        topPanelDiscussion.setBackground(Color.WHITE);
        topPanelDiscussion.setBounds(0, 10, 1430, 40); // ✅ Required for null layout

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10); // padding

        // Back Button
        BackButtonPanelGUI backButtonPanel = new BackButtonPanelGUI();
        JButton backButton = backButtonPanel.getBackButton();
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Home"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        topPanelDiscussion.add(backButton, gbc);

        // Create a panel to hold the back button and add it to the UI
        add(backButtonPanel, BorderLayout.NORTH);

        // Title Label
        JLabel titleLabel = new JLabel("Reservation Guidelines", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));

        gbc.gridx = 1; // ✅ Middle
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);
        topPanelDiscussion.add(titleLabel, gbc);

        // Scrollable Text Area
        JEditorPane guidelinePane = new JEditorPane();
        guidelinePane.setContentType("text/html");
        guidelinePane.setEditable(false);
        guidelinePane.setText(
            "<html><body style='font-family: Arial; font-size: 15px;'>"+
            "<b>1. No Overlap-Booking:</b><br>" +
            "&nbsp;&nbsp;- You <b>cannot reserve overlapping time slots</b>, even in different rooms.<br>" +
            "&nbsp;&nbsp;- <b>Back-to-back bookings</b> (e.g., <b>9–10 AM</b> then <b>10–11 AM</b>) are <b>allowed</b>.<br><br>" +

            "<b>2. Daily Limit:</b><br>" +
            "&nbsp;&nbsp;- Each user can only reserve a <b>total of 4 hours per day</b>.<br><br>" +

            "<b>3. Max 2 Reservations Per Day:</b><br>" +
            "&nbsp;&nbsp;- You can only make <b>up to 2 reservations per day</b>.<br><br>" +

            "<b>4. Advance Reservation Required:</b><br>" +
            "&nbsp;&nbsp;- You can only reserve a room <b>at least 2 hours in advance</b> from the current time.<br><br>" +

            "<b>5. Refresh Required After Reservation:</b><br>" +
            "&nbsp;&nbsp;- After making a reservation, <b>you must exit and relaunch the system</b> to view your updated reservations in table.<br><br>" +

            "<b>6. Cancellation Policy:</b><br>" +
            "&nbsp;&nbsp;- To cancel a reservation, <b>please contact the staff management</b> at <b>012-123-1234 or 013-123-1234</b> for permission.<br>" +
            "</html>"
        );
        guidelinePane.setFont(new Font("Arial", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(guidelinePane);
        scrollPane.setBounds(50, 100, 1330, 450); // Lowered for spacing
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Add components to main panel
        add(topPanelDiscussion);
        add(scrollPane);
    }
}
