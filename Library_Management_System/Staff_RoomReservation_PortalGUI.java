import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Staff_RoomReservation_PortalGUI {
    private JPanel cardPanel;
    private DefaultTableModel pendingTableModel;
    private DefaultTableModel confirmedTableModel;
    private JTable pendingTable;
    private JTable confirmedTable;
    private JFrame frame;
    private String currentTableView = "Pending"; // Default view

    public Staff_RoomReservation_PortalGUI(StaffData staff) {
        frame = new JFrame("Staff Reservation Portal");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Top Panel (Title + Buttons)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Title at the top center
        JLabel titleLabel = new JLabel("Upcoming Room Reservations", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // JToggleButtons
        JToggleButton pendingButton = new JToggleButton("Pending Records");
        JToggleButton confirmedButton = new JToggleButton("Confirmed Record");

        // Font styling
        Font tabFont = new Font("Segoe UI", Font.BOLD, 14);
        pendingButton.setFont(tabFont);
        confirmedButton.setFont(tabFont);

        pendingButton.setFocusPainted(false);
        confirmedButton.setFocusPainted(false);

        // Button group
        ButtonGroup tabGroup = new ButtonGroup();
        tabGroup.add(pendingButton);
        tabGroup.add(confirmedButton);
        pendingButton.setSelected(true);

        // Size and border
        Dimension tabSize = new Dimension(160, 40);
        pendingButton.setPreferredSize(tabSize);
        confirmedButton.setPreferredSize(tabSize);

        pendingButton.setBorder(BorderFactory.createMatteBorder(2, 2, 0, 1, Color.GRAY));
        confirmedButton.setBorder(BorderFactory.createMatteBorder(2, 1, 0, 2, Color.GRAY));

        // Tab button panel
        JPanel tabButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        tabButtonPanel.add(pendingButton);
        tabButtonPanel.add(confirmedButton);

        // Separator line
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.BLACK);
        separator.setPreferredSize(new Dimension(322, 2)); // match buttons width

        // Container to hold buttons and separator in vertical stack
        JPanel tabContainer = new JPanel();
        tabContainer.setLayout(new BoxLayout(tabContainer, BoxLayout.Y_AXIS));
        tabContainer.add(tabButtonPanel);
        tabContainer.add(separator);

        // Add to topPanel
        topPanel.add(tabContainer, BorderLayout.SOUTH);

        // Add the combined panel to the top
        frame.add(topPanel, BorderLayout.NORTH);

        // Card Panel for switching tables
        cardPanel = new JPanel(new CardLayout());

        // Shared renderer
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Column names
        String[] columnNames = {
            "ReservationID", "UserID", "Username", "Contact", "RoomID", "Date",
            "Duration", "No. of Pax", "Equipments", "Status"
        };

        // Pending Table
        pendingTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 9;  // Status column is editable
            }
        };
        pendingTable = new JTable(pendingTableModel);
        pendingTable.getTableHeader().setReorderingAllowed(false);
        ReservationTableManager.setupTable(pendingTable, "Pending");

        // Confirmed Table 
        confirmedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return column == 9;  // Only Status column is editable
            }
        };
        confirmedTable = new JTable(confirmedTableModel);
        confirmedTable.getTableHeader().setReorderingAllowed(false);
        ReservationTableManager.setupTable(confirmedTable, "Confirmed");

        // Panel with Title and Reminder above Confirmed Table
        JPanel confirmedPanelWithTitle = new JPanel(new BorderLayout());

        // Title
        JLabel confirmedTitleLabel = new JLabel("Confirmed Reservation Records", SwingConstants.CENTER);
        confirmedTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        confirmedTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmedTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Reminder
        JLabel reminderLabel = new JLabel("Reminder: Once saved as 'Finished' or 'Cancelled', the reservation cannot be undone or reverted.", SwingConstants.CENTER);
        reminderLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        reminderLabel.setForeground(Color.RED);
        confirmedTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reminderLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // Stack title + reminder vertically (centered)
        JPanel titleReminderPanel = new JPanel();
        titleReminderPanel.setLayout(new BoxLayout(titleReminderPanel, BoxLayout.Y_AXIS));
        titleReminderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmedTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reminderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleReminderPanel.add(Box.createVerticalStrut(10)); // spacing above
        titleReminderPanel.add(confirmedTitleLabel);
        titleReminderPanel.add(Box.createVerticalStrut(5));  // spacing between title and reminder
        titleReminderPanel.add(reminderLabel);
        titleReminderPanel.add(Box.createVerticalStrut(10)); // spacing below

        // Add top and table to main panel
        confirmedPanelWithTitle.add(titleReminderPanel, BorderLayout.NORTH);
        JPanel confirmedTableContainer = new JPanel(new BorderLayout());
        confirmedTableContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // Add spacing
        confirmedTableContainer.add(new JScrollPane(confirmedTable), BorderLayout.CENTER);
        confirmedPanelWithTitle.add(confirmedTableContainer, BorderLayout.CENTER);

        // Panel with Title and Reminder above Pending Table
        JPanel pendingPanelWithTitle = new JPanel(new BorderLayout());

        // Title
        JLabel pendingTitleLabel = new JLabel("Pending Reservation Records", SwingConstants.CENTER);
        pendingTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        pendingTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pendingTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Reminder
        JLabel pendingReminderLabel = new JLabel("Reminder: Once saved as 'Cancelled', the reservation cannot be undone or reverted.", SwingConstants.CENTER);
        pendingReminderLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        pendingReminderLabel.setForeground(Color.RED);
        pendingReminderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pendingReminderLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // Stack title + reminder vertically
        JPanel pendingTitleReminderPanel = new JPanel();
        pendingTitleReminderPanel.setLayout(new BoxLayout(pendingTitleReminderPanel, BoxLayout.Y_AXIS));
        pendingTitleReminderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pendingTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pendingReminderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        pendingTitleReminderPanel.add(Box.createVerticalStrut(10));
        pendingTitleReminderPanel.add(pendingTitleLabel);
        pendingTitleReminderPanel.add(Box.createVerticalStrut(5));
        pendingTitleReminderPanel.add(pendingReminderLabel);
        pendingTitleReminderPanel.add(Box.createVerticalStrut(10));

        // Add top and table to pending panel
        pendingPanelWithTitle.add(pendingTitleReminderPanel, BorderLayout.NORTH);
        JPanel pendingTableContainer = new JPanel(new BorderLayout());
        pendingTableContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30)); // top, left, bottom, right
        pendingTableContainer.add(new JScrollPane(pendingTable), BorderLayout.CENTER);
        pendingPanelWithTitle.add(pendingTableContainer, BorderLayout.CENTER);

        // Add to card panel
        cardPanel.add(pendingPanelWithTitle, "Pending");
        cardPanel.add(confirmedPanelWithTitle, "Confirmed");

        frame.add(cardPanel, BorderLayout.CENTER);

        // South panel with Save button
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        saveButton.setBackground(new Color(0, 153, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(120, 40));
        southPanel.add(saveButton);
        frame.add(southPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to save changes?", 
                "Confirm Save", 
                JOptionPane.YES_NO_OPTION);
        
            if (confirm == JOptionPane.YES_OPTION) {
                if (currentTableView.equals("Pending")) {
                    ReservationTableManager.updateReservationStatus(pendingTableModel, "Pending");
                    JOptionPane.showMessageDialog(frame, "The record changes have been saved successfully.");
                    JOptionPane.showMessageDialog(frame, "Relaunch the reservation portal to see updates in table.");
                } else if (currentTableView.equals("Confirmed")) {
                    ReservationTableManager.updateReservationStatus(confirmedTableModel, "Confirmed");
                    JOptionPane.showMessageDialog(frame, "The record changes have been saved successfully.");
                    JOptionPane.showMessageDialog(frame, "Relaunch the reservation portal to see updates in table.");
                }
            }
        });

        // Load reservation data for Pending and Confirmed
        ReservationTableManager.loadReservationsByStatus("Reservation.txt", pendingTableModel, "Pending");
        ReservationTableManager.loadReservationsByStatus("Reservation.txt", confirmedTableModel, "Confirmed");

        CardLayout cl = (CardLayout)(cardPanel.getLayout());

        pendingButton.setBackground(Color.WHITE);
        confirmedButton.setBackground(Color.WHITE);

        pendingButton.addActionListener(e -> {
            confirmedButton.setBackground(Color.WHITE);
            cl.show(cardPanel, "Pending");
            ReservationTableManager.updateStatusDropdownStaff(pendingTable, "Pending");
            currentTableView = "Pending";
        });

        confirmedButton.addActionListener(e -> {
            pendingButton.setBackground(Color.WHITE);
            cl.show(cardPanel, "Confirmed");
            ReservationTableManager.updateStatusDropdownStaff(confirmedTable, "Confirmed");
            currentTableView = "Confirmed";
        });

        // Add to frame
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new Staff_HomePage(staff);
            }
        });
    }
}