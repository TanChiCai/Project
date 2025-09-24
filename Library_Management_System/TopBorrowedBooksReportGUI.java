import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;

import java.io.File;

import javax.imageio.ImageIO;




import java.awt.image.BufferedImage;

import java.awt.event.*;

public class TopBorrowedBooksReportGUI extends JFrame {
    private JLabel titleLabel;
    public TopBorrowedBooksReportGUI(StaffData staff) {
        setTitle("Top Borrowed Books Report");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Top 5 Borrowed Books Report", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SimSun", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        topPanel.add(titleLabel);

        // 👇 ADD: Year selection
        JPanel yearPanel = new JPanel();
        JLabel yearLabel = new JLabel("Select Year: ");
        yearLabel.setFont(new Font("SimSun", Font.BOLD, 20));
        JComboBox<Integer> yearComboBox = new JComboBox<>();

        // Populate yearComboBox dynamically from borrowed list
        Set<Integer> yearSet = new TreeSet<>();
        for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
            yearSet.add(borrow.getBorrowDate().getYear()); // assuming getBorrowDate() returns a LocalDate
        }
        for (int year : yearSet) {
            yearComboBox.addItem(year);
        }
        yearPanel.add(yearLabel);
        yearPanel.add(yearComboBox);
        topPanel.add(yearPanel);

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

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel infoLabel = new JLabel("");
        infoLabel.setFont(new Font("SimSun", Font.PLAIN, 18));
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoLabel.setVisible(false);
        centerPanel.add(infoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel yearInfoLabel = new JLabel("");
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
            titleLabel.setText("Top 5 Borrowed Books Report in " + selectedYear);

            String[] columns = {"Rank", "Title", "Author", "Times Borrowed", "Genre", "Year Published"};
            HashMap<Book, Integer> bookCounts = new HashMap<>();

           
            for (BorrowDetail borrowBook : BookFileManager.getBorrowedList()) {
                
                if (borrowBook.getBorrowDate().getYear() != selectedYear) continue;

                Book book = borrowBook.getBook();
                bookCounts.put(book, bookCounts.getOrDefault(book, 0) + 1);
            }

            List<Map.Entry<Book, Integer>> sortedList = new ArrayList<>(bookCounts.entrySet());
            sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            int limit = Math.min(5, sortedList.size());
            Object[][] data = new Object[limit][6];

            for (int i = 0; i < limit; i++) {
                Book borrowedBook = sortedList.get(i).getKey();
                int timesBorrowed = sortedList.get(i).getValue();

                data[i][0] = i + 1;
                data[i][1] = borrowedBook.getBookName();
                data[i][2] = borrowedBook.getAuthor();
                data[i][3] = timesBorrowed;
                data[i][4] = borrowedBook.getGenre();
                data[i][5] = borrowedBook.getPublicationYear();
            }

            infoLabel.setText("Displaying top 5 most borrowed books.");
            infoLabel.setFont(new Font("SimSun", Font.BOLD, 20));
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

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(60);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(150);
            columnModel.getColumn(3).setPreferredWidth(130);
            columnModel.getColumn(4).setPreferredWidth(120);
            columnModel.getColumn(5).setPreferredWidth(120);

            // Center align all columns
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // ✅ Enable multi-line for book titles
            table.getColumnModel().getColumn(1).setCellRenderer(new MultiLineCellRenderer());
            for (int row = 0; row < table.getRowCount(); row++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, 1), row, 1);
                table.setRowHeight(row, Math.max(30, comp.getPreferredSize().height));
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