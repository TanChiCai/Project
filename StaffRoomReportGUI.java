import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class StaffRoomReportGUI extends JFrame {
    public StaffRoomReportGUI(StaffData staff) {
        setTitle("Room Report");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel (title + input + button)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Yearly Number of Reserved Rooms Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel);

        // Input panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Font comboFont = new Font("Arial", Font.PLAIN, 18);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(labelFont);

        JComboBox<Integer> yearComboBox = new JComboBox<>();
        yearComboBox.setFont(comboFont);
        yearComboBox.setPreferredSize(new Dimension(100, 35));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 1; i <= currentYear; i++) {
            yearComboBox.addItem(i);
        }

        inputPanel.add(yearLabel);
        inputPanel.add(yearComboBox);
        topPanel.add(inputPanel);

        // Generate button
        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("Arial", Font.BOLD, 18));
        generateButton.setBackground(new Color(34, 139, 34));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setPreferredSize(new Dimension(150, 40));
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateButton.setMaximumSize(new Dimension(150, 40));
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        topPanel.add(generateButton);

        // Space between button and table
        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel inside scroll box
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Year label (initially hidden)
        JLabel yearInfoLabel = new JLabel("");
        yearInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        yearInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearInfoLabel.setVisible(false);
        centerPanel.add(yearInfoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        centerPanel.add(tablePanel);

        // Generate button action
        generateButton.addActionListener(e -> {
            int selectedYear = (int) yearComboBox.getSelectedItem();

            // Call external class method
            int[][] summary = ReservationTableManager.getMonthlyReservationByYear(selectedYear);

            yearInfoLabel.setText("Showing report for year: " + selectedYear);
            yearInfoLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Make the text bigger
            yearInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Left align the label
            yearInfoLabel.setVisible(true);

            // Adjust panel properties for proper left alignment
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10)); // Left align the label within the panel
            labelPanel.add(yearInfoLabel);

            // Prepare column names
            String[] columns = new String[14];
            columns[0] = "Rooms";
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            System.arraycopy(months, 0, columns, 1, 12);
            columns[13] = "Total";

            // Fill table data
            DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // All cells are non-editable
                }
            };

            String[] discussionRow = new String[14];
            discussionRow[0] = "Discussion Room";
            for (int i = 0; i < 12; i++) {
                discussionRow[i + 1] = String.valueOf(summary[0][i]);
            }
            discussionRow[13] = String.valueOf(summary[0][12]);

            String[] conferenceRow = new String[14];
            conferenceRow[0] = "Conference Room";
            for (int i = 0; i < 12; i++) {
                conferenceRow[i + 1] = String.valueOf(summary[1][i]);
            }
            conferenceRow[13] = String.valueOf(summary[1][12]);

            tableModel.addRow(discussionRow);
            tableModel.addRow(conferenceRow);

            // Grand Total row
            String[] grandTotalRow = new String[14];
            grandTotalRow[0] = "Grand Total";
            int grandTotal = 0;
            for (int i = 0; i < 12; i++) {
                int sum = summary[0][i] + summary[1][i];
                grandTotalRow[i + 1] = String.valueOf(sum);
                grandTotal += sum;
            }
            grandTotalRow[13] = String.valueOf(grandTotal);
            tableModel.addRow(grandTotalRow);

            JTable table = new JTable(tableModel);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 17));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
            table.getTableHeader().setReorderingAllowed(false);

            // Set column widths
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200); // Room Type
            for (int i = 1; i <= 12; i++) {
                columnModel.getColumn(i).setPreferredWidth(80); // Months
            }
            columnModel.getColumn(13).setPreferredWidth(150); // Total

            // Center align all cells
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Wrap table in scroll pane and fit size
            JScrollPane scrollPane = new JScrollPane(table);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            // Update table panel
            tablePanel.removeAll();
            tablePanel.add(labelPanel, BorderLayout.NORTH);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            tablePanel.revalidate();
            tablePanel.repaint();
        });

        add(mainPanel);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new Staff_HomePage(staff);
            }
        });
    }
}