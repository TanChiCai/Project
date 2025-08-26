import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;

public class TopBorrowingUsersReport extends JFrame {
    private JLabel titleLabel;

    public TopBorrowingUsersReport(StaffData staff) {
        setTitle("Top Borrowing Users Report");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Top 5 Users Who Borrowed the Most Books", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SimSun", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel);

        // Year selection
        JPanel yearPanel = new JPanel();
        JLabel yearLabel = new JLabel("Select Year: ");
        yearLabel.setFont(new Font("SimSun", Font.BOLD, 20));
        JComboBox<Integer> yearComboBox = new JComboBox<>();

        // Populate years
        Set<Integer> yearSet = new TreeSet<>();
        for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
            yearSet.add(borrow.getBorrowDate().getYear());
        }
        for (int year : yearSet) {
            yearComboBox.addItem(year);
        }
        yearPanel.add(yearLabel);
        yearPanel.add(yearComboBox);
        topPanel.add(yearPanel);

        // Generate button
        JButton generateButton = new JButton("Generate Report");
        generateButton.setFont(new Font("SimSun", Font.BOLD, 18));
        generateButton.setBackground(new Color(34, 139, 34));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFocusPainted(false);
        generateButton.setPreferredSize(new Dimension(200, 40));
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(generateButton);
        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel();
        infoLabel.setFont(new Font("SimSun", Font.BOLD, 20));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setVisible(false);
        centerPanel.add(infoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel yearInfoLabel = new JLabel();
        yearInfoLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        yearInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearInfoLabel.setVisible(false);
        centerPanel.add(yearInfoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel tablePanel = new JPanel(new BorderLayout());
        centerPanel.add(tablePanel);

        generateButton.addActionListener(e -> {
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            if (selectedYear == null) return;

            titleLabel.setText("Top 5 Users Who Borrowed the Most Books in " + selectedYear);

            String[] columns = {"Rank", "User ID", "Name", "Books Borrowed"};
            HashMap<UserData, Integer> userCounts = new HashMap<>();

            for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
                if (borrow.getBorrowDate().getYear() != selectedYear) continue;
                UserData user = borrow.getUser();
                userCounts.put(user, userCounts.getOrDefault(user, 0) + 1);
            }

            List<Map.Entry<UserData, Integer>> sortedUsers = new ArrayList<>(userCounts.entrySet());
            sortedUsers.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            int limit = Math.min(5, sortedUsers.size());
            Object[][] data = new Object[limit][4];
            for (int i = 0; i < limit; i++) {
                UserData user = sortedUsers.get(i).getKey();
                int count = sortedUsers.get(i).getValue();
                data[i][0] = i + 1;
                data[i][1] = user.getUserid();
                data[i][2] = user.getName();
                data[i][3] = count;
            }

            infoLabel.setText("Displaying top 5 users with the most borrowed books.");
            infoLabel.setVisible(true);

            yearInfoLabel.setText("Year selected: " + selectedYear);
            yearInfoLabel.setVisible(true);

            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
            labelPanel.add(infoLabel);
            labelPanel.add(Box.createHorizontalStrut(20));
            labelPanel.add(yearInfoLabel);

            DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(tableModel);
            table.setFont(new Font("SimSun", Font.PLAIN, 17));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
            table.getTableHeader().setReorderingAllowed(false);

            // Set column widths
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(60);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(200);
            columnModel.getColumn(3).setPreferredWidth(150);

            // Center align all columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                columnModel.getColumn(i).setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            tablePanel.removeAll();
            tablePanel.add(labelPanel, BorderLayout.NORTH);
            tablePanel.add(scrollPane, BorderLayout.CENTER);
            tablePanel.revalidate();
            tablePanel.repaint();
        });

        add(mainPanel);
        setVisible(true);
    }

    // Optional multiline renderer if needed for future expansion
    class MultiLineCellRenderer extends JTextArea implements javax.swing.table.TableCellRenderer {
        public MultiLineCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString());
            setFont(table.getFont());
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
            return this;
        }
    }
}
