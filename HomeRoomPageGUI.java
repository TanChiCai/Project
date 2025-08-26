import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeRoomPageGUI extends JPanel {

    public HomeRoomPageGUI(CardLayout cardLayout, JPanel cardPanel, UserData currentUser) {
        setLayout(null);
        setBackground(Color.WHITE);

        JLabel label = new JLabel("Room Reservation", SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 36));
        label.setForeground(Color.BLACK);
        label.setBounds(0, 20, 1430, 50);

        JButton newBookingButton = new JButton("+ New Booking ▼"); // down arrow
        newBookingButton.setFont(new Font("Arial", Font.BOLD, 16));
        newBookingButton.setBounds(30, 100, 170, 40);
        newBookingButton.setBackground(new Color(30, 144, 255)); // blue color
        newBookingButton.setForeground(Color.WHITE);
        newBookingButton.setFocusPainted(false);

        // Create popup menu
        JPopupMenu bookingMenu = new JPopupMenu();
        JMenuItem discussionRoomItem = new JMenuItem("Discussion Room");
        JMenuItem conferenceRoomItem = new JMenuItem("Conference Room");

        bookingMenu.add(discussionRoomItem);
        bookingMenu.add(conferenceRoomItem);

        // Show popup menu when button is clicked
        newBookingButton.addActionListener(e -> {
            int x = newBookingButton.getWidth() - bookingMenu.getPreferredSize().width;
            int y = newBookingButton.getHeight();
            bookingMenu.show(newBookingButton, x, y);
        });

        // Actions to change cards
        discussionRoomItem.addActionListener(e -> cardLayout.show(cardPanel, "DiscussionRoom"));
        conferenceRoomItem.addActionListener(e -> cardLayout.show(cardPanel, "ConferenceRoom"));

        // "Reserve Guideline" button
        JButton guidelineButton = new JButton("Reserve Guideline");
        guidelineButton.setFont(new Font("Arial", Font.BOLD, 16));
        guidelineButton.setBounds(220, 100, 180, 40); // Positioned to the right of newBookingButton
        guidelineButton.setBackground(new Color(34, 139, 34)); // green color
        guidelineButton.setForeground(Color.WHITE);
        guidelineButton.setFocusPainted(false);

        // Action to switch to guideline page
        guidelineButton.addActionListener(e -> cardLayout.show(cardPanel, "ReserveGuideline"));

        ReservationTableManager tableManager = new ReservationTableManager();
        String userID = currentUser.getUserid();
        DefaultTableModel tableModel = tableManager.getUserRoomReservationsTableModel(userID);

        JTable reservationTable = new JTable(tableModel);
        reservationTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        reservationTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        reservationTable.setFillsViewportHeight(true); // fills vertical space
        reservationTable.setRowHeight(30);             // make rows taller
        reservationTable.setShowVerticalLines(false);  // cleaner look when no data
        reservationTable.setShowGrid(true);            // show grid lines
        reservationTable.getTableHeader().setReorderingAllowed(false);

        // Center-align cell content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < reservationTable.getColumnCount(); i++) {
            reservationTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane tableScroll = new JScrollPane(reservationTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder()); // clean border

        // Create a panel to wrap the table with a box
        JPanel tableBoxPanel = new JPanel(new BorderLayout());
        tableBoxPanel.setBounds(30, 160, 1350, 400); // size and position
        tableBoxPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // adds a visible box
        tableBoxPanel.setBackground(Color.WHITE); // match background

        // Show either table or "no data" message inside the box
        if (tableModel.getRowCount() == 0) {
            JLabel emptyLabel = new JLabel("No reservation in table", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 20));
            emptyLabel.setForeground(Color.DARK_GRAY);
            tableBoxPanel.add(emptyLabel, BorderLayout.CENTER);
        } else {
            tableBoxPanel.add(tableScroll, BorderLayout.CENTER);
        }

        add(label);
        add(newBookingButton);
        add(guidelineButton);
        add(tableBoxPanel); // add the wrapper panel to the frame
    }
}