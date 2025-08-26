import java.time.Period;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;

import java.util.Locale;

import java.awt.event.ActionEvent;
import javax.swing.table.*;


public class Staff_HomePage extends JFrame {
    private boolean isMenuVisible = false;
    private boolean isReportSubMenuVisible = false;
    private final JPanel sidePanel;
    private final JPanel reportSubPanel; // panel to hold sub-report buttons
    private StaffData currentStaff;
    private JFrame profileFrame;

    public Staff_HomePage(StaffData Staff) {
        this.currentStaff=Staff;

        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        setTitle("Staff Homepage");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.decode("#fefefe"));

        // Homepage background
        JLabel backgroundPanel = GUI.createBackgroundLabel("Image/homepageImage.jpg", screenSize);
        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(screenSize);
        add(backgroundPanel);
        setComponentZOrder(backgroundPanel, 0);

        // Top Bar
        JPanel topBar = new JPanel(null);
        topBar.setBackground(new Color(60, 90, 153));
        topBar.setBounds(0, 0, screenWidth, 60);

        JButton menuButton = GUI.createButton("MENU", 10, 10, 80, 40, 14);
        topBar.add(menuButton);

        JLabel title = GUI.createLabel("Library", (screenWidth / 2) - 75, 15, 150, 30, 22);
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(title);

        JButton staffIDBtn = GUI.createStaffIdButton(currentStaff.getStaffid().toUpperCase(), "Image/profile.png", screenWidth - 140, 10, 130, 40);
        
        staffIDBtn.addActionListener(e -> {
            showProfilePopup(currentStaff, "Image/profile.png");
        });

        topBar.add(staffIDBtn);

        backgroundPanel.add(topBar);

        // Side Panel
        sidePanel = new JPanel(null);
        sidePanel.setBackground(new Color(240, 240, 240));
        sidePanel.setBounds(-300, 60, 300, screenHeight - 60);

        int yOffset = 20;
        addSidebarButton("Book List", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Add New Book", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Borrow Book", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Return Book", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Borrow Book Management", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Reserve Book Management", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Reserve Room", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Fine", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        addSidebarButton("Black List", yOffset, currentStaff.getStaffid());
        yOffset += 50;

        // Report button
        JButton reportBtn = GUI.createButton("Report", 10, yOffset, 260, 30, 15);
        reportBtn.addActionListener(e -> toggleReportSubMenu());
        sidePanel.add(reportBtn);
        yOffset += 40;

        // Sub-Report Panel
        reportSubPanel = new JPanel();
        reportSubPanel.setLayout(new BoxLayout(reportSubPanel, BoxLayout.Y_AXIS));
        reportSubPanel.setBounds(20, yOffset, 240, 0); 
        reportSubPanel.setBackground(new Color(220, 220, 220));
        reportSubPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); 
        reportSubPanel.setVisible(false);
        sidePanel.add(reportSubPanel);

        // Add sub-report buttons with smaller vertical spacing
        addSubReportButton("User Report", currentStaff.getStaffid());
        addSubReportButton("Book Report", currentStaff.getStaffid());
        addSubReportButton("Room Report", currentStaff.getStaffid());  
        addSubReportButton("Fine Report", currentStaff.getStaffid());   
        yOffset += 10;  

        addSidebarButton("Log Out", yOffset, currentStaff.getStaffid());
        yOffset += 50;
        
        backgroundPanel.add(sidePanel);

        menuButton.addActionListener(e -> toggleMenu());

        setVisible(true);
    }

    public void showProfilePopup(StaffData staffID, String imagePath) {

        if (profileFrame != null) {
            profileFrame.dispose();
        }

        profileFrame = new JFrame("Profile");
        profileFrame.setSize(300, 400);
        profileFrame.setLayout(new BorderLayout());
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setLocationRelativeTo(null);
    
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    
        String[] labels = {"Staff ID:", "Name:", "Phone Number:"};
        String[] values = {staffID.getStaffid(), staffID.getName(), staffID.getPhoneNumber()};
        
        for (int i = 0; i < labels.length; i++) {
            JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            detailPanel.setMaximumSize(new Dimension(280, 40));
            
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("SimSun", Font.BOLD, 14));
            detailPanel.add(label);
            
            JLabel value = new JLabel(values[i]);
            value.setFont(new Font("SimSun", Font.PLAIN, 14));
            detailPanel.add(value);
            
            contentPanel.add(detailPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    
        profileFrame.add(contentPanel, BorderLayout.CENTER);
        profileFrame.setVisible(true);
    }

    private void addSidebarButton(String name, int yOffset, String staffID) {
        JButton btn = GUI.createButton(name, 10, yOffset, 260, 30, 15);
        btn.addActionListener(e -> handleSidebarAction(name, staffID));
        sidePanel.add(btn);
    }

    // addSubReportButton method
    private void addSubReportButton(String name, String staffID) {
        JButton btn = new JButton(name);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);  
        btn.setMaximumSize(new Dimension(230, 25));   
        btn.setFont(new Font("SimSun", Font.BOLD, 13));  
        btn.setMargin(new Insets(2, 5, 2, 5));
        btn.addActionListener(e -> handleSubReportAction(name, staffID));
        reportSubPanel.add(btn);
        reportSubPanel.add(Box.createRigidArea(new Dimension(0, 3)));
    }

    private void toggleMenu() {
        int targetX = isMenuVisible ? -300 : 0;
        sidePanel.setLocation(targetX, 60);
        isMenuVisible = !isMenuVisible;
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    // toggleReportSubMenu method 
    private void toggleReportSubMenu() {
        isReportSubMenuVisible = !isReportSubMenuVisible;
        reportSubPanel.setVisible(isReportSubMenuVisible);
        
        // Calculate height based on number of buttons
        int buttonCount = reportSubPanel.getComponentCount() / 2;
        int newHeight = isReportSubMenuVisible ? (buttonCount * 28) + 10 : 0;
        
        reportSubPanel.setBounds(20, reportSubPanel.getY(), 240, newHeight);
        
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void handleSidebarAction(String action, String staffID) {
        switch (action) {
            case "Book List":
    
                new BookManagementPage(currentStaff);
                break;
            case "Add New Book":
     
                new AddNewBook(currentStaff);
                break;
            case "Borrow Book":
            
                new BorrowBookForm(currentStaff);
                break;
            case "Return Book":
   
                new ReturnBookForm(currentStaff);
                break;
            case "Reserve Book Management":
      
                new StaffReserveBookManagement(currentStaff);
                break;  
            case "Borrow Book Management":
 
                new staffBorrowBookManagement(currentStaff);
                break;
            case "Reserve Room":
                dispose();
                new Staff_RoomReservation_PortalGUI(currentStaff);
                break;
            case "Fine":
   
                List<ProcessFineDetail> processFine = new ArrayList<>();
                processFine=ProcessFineStaff.loadProcessFineFromFile("processFine.txt");
                new ProcessFineStaff(processFine);
                break;
            case "Black List":
                List<BlackListDetail> Blacklist = new ArrayList<>();
                Blacklist=BlackList.loadBlacklist("Blacklist.txt");
                new BlackList(Blacklist);
                break;
            case "Log Out":
                dispose();
                new Staff_LoginPage();
                break;
        }
    }

    private void handleSubReportAction(String reportName, String staffID) {
        switch (reportName) {
            case "User Report":
                new TopBorrowingUsersReport(this.currentStaff);
                break;        
            case "Book Report":
                new TopBorrowedBooksReportGUI(this.currentStaff);
                break;
            case "Room Report":
                dispose();
                new StaffRoomReportGUI(this.currentStaff);
                break;
            case "Fine Report":
                List<ProcessFineDetail> processFine = new ArrayList<>();
                processFine=ProcessFineStaff.loadProcessFineFromFile("processFine.txt");
                new ProcessFineReport(processFine);
                break;
            default:
                JOptionPane.showMessageDialog(this, reportName + " Page (Coming Soon)");
                break;
        }
    }

}


class BorrowBookForm extends JFrame {
    private JComponent[] borrowComponents;
    private JTextField userIdField,bookIdField;
    private LocalDate borrowDate,dueDate;
    private List<BorrowDetail> borrowedList=new ArrayList<>();;
    private ArrayList<UserData> users = new ArrayList<>();
    private List<ReserveBookDetail> reservedList=new ArrayList<>();;
    private List<Book> bookList = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public BorrowBookForm(StaffData currentStaff) {
           
        setTitle("Staff Borrow Book Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window
        setUndecorated(false); // Set to true if you want to remove the title bar (optional)



        

        reservedList=BookFileManager.getReserveBookList();
        bookList=BookFileManager.getBookList();
        borrowedList=BookFileManager.getBorrowedList();
        users=UserFileManager.loadAllUsers();
        
        Font labelFont = new Font("SimSun", Font.PLAIN, 22);
        Font fieldFont = new Font("SimSun", Font.PLAIN, 20); 
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(50, 50, 50, 50)); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.insets = new Insets(15, 15, 15, 15);

        // Title
        JLabel title = new JLabel("Staff Borrow Book Page", SwingConstants.CENTER);
        title.setFont(new Font("SimSun", Font.BOLD, 28));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // userId
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(labelFont);
        panel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField();
        userIdField.setFont(fieldFont);
        userIdField.setPreferredSize(new Dimension(350, 40)); // Increased size of the text field
        addSmartPlaceholder(userIdField, "ex:U0001");
        panel.add(userIdField, gbc);

        // Book ID
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel bookLabel = new JLabel("Book ID:");
        bookLabel.setFont(labelFont);
        panel.add(bookLabel, gbc);

        
        gbc.gridx = 1;
        bookIdField = new JTextField();
        bookIdField.setFont(fieldFont);
        bookIdField.setPreferredSize(new Dimension(350, 40));
        addSmartPlaceholder(bookIdField, "ex:BK0001");
        panel.add(bookIdField, gbc);


        borrowComponents = new JComponent[] {
            userIdField,
            bookIdField
        };

        for (int i = 0; i < borrowComponents.length; i++) {
            int index = i;
            JTextField field = (JTextField) borrowComponents[i];
            field.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN && index < borrowComponents.length - 1) {
                        borrowComponents[index + 1].requestFocus();
                    } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP && index > 0) {
                        borrowComponents[index - 1].requestFocus();
                    }
                }
            });
            field.addActionListener(e -> validateForm()); // Pressing Enter triggers validateForm()

        }


        


        // Borrow Date
        gbc.gridx = 0;
        gbc.gridy++;
        
        JLabel borrowLabel = new JLabel("Borrow Date:");
        borrowLabel.setFont(labelFont);
        panel.add(borrowLabel, gbc);

        

        gbc.gridx = 1;
        borrowDate= LocalDate.now();
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedborrowDate = borrowDate.format(FORMATTER);
        JLabel borrowDateValueLabel = new JLabel(formattedborrowDate);
        borrowDateValueLabel.setFont(fieldFont);
        borrowDateValueLabel.setPreferredSize(new Dimension(350, 50)); // Optional styling
        panel.add(borrowDateValueLabel, gbc);

        // Due Date
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dueDateLabel = new JLabel("Due Date:");
        dueDateLabel.setFont(labelFont);
        panel.add(dueDateLabel, gbc);

        
        gbc.gridx++;
        dueDate = borrowDate.plusDays(6);
        String formatteddueDate = dueDate.format(FORMATTER);
        JLabel dueDateValueLabel = new JLabel(formatteddueDate);
        dueDateValueLabel.setFont(fieldFont);
        dueDateValueLabel.setPreferredSize(new Dimension(350, 50)); // Optional styling
        panel.add(dueDateValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton borrowButton = new JButton("Borrow Books");
        borrowButton.setFont(new Font("SimSun", Font.BOLD, 24)); // Larger font for button text
        borrowButton.setPreferredSize(new Dimension(300, 70)); // Increased size of the button
        borrowButton.setBackground(new Color(76, 175, 80));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFocusPainted(false);
        borrowButton.addActionListener(e -> validateForm());
        panel.add(borrowButton, gbc);

        

        add(panel);

        getRootPane().setDefaultButton(borrowButton);

        setVisible(true); 
    }

    public static void addSmartPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder); // Set the placeholder initially
    
        final boolean[] ISPLACEHOLDERACTIVE  = {true};  // This flag tracks the placeholder state
    
        // When the user types something
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (ISPLACEHOLDERACTIVE[0]) {
                    textField.setText("");  // Clear the placeholder when the user types
                    textField.setForeground(Color.BLACK);  // Change text color to black for actual input
                    ISPLACEHOLDERACTIVE[0] = false;
                }
            }
        });
    
        // When the user clicks away or comes back to the field
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);  // Restore placeholder if input is empty
                    ISPLACEHOLDERACTIVE[0] = true;
                }
            }
    
            public void focusGained(java.awt.event.FocusEvent e) {
                if (ISPLACEHOLDERACTIVE[0]) {
                    textField.setCaretPosition(0);
                }
            }
            
        });
    }
    


    private void validateForm() {
        
        boolean valid = true;
        StringBuilder errorMessage = new StringBuilder();
        String bookIdPattern = "(?i)^BK\\d{4}$";  // e.g. BK0001
        String userIdPattern = "(?i)^U\\d{4}$";   // e.g. U0001
        
        String userIdText = userIdField.getText().trim();
        String bookIdText = bookIdField.getText().trim();

        List<BlackListDetail> blacklist = BlackList.loadBlacklist("Blacklist.txt"); // Adjust filename if needed
        for (BlackListDetail detail : blacklist) {
            if (detail.getUser().getUserid().equalsIgnoreCase(userIdText) && detail.isBlacklisted()) {
                errorMessage.append("- User is blacklisted and cannot perform this action.\n");
                valid = false;
                break;
            }
        }

        // Validation
        if (userIdText.isEmpty()) {  // check for placeholder
            errorMessage.append("- Please enter user ID.\n");
            valid = false;
        } else if (!userIdText.matches(userIdPattern)) {
            errorMessage.append("- User ID must be in the format U0001.\n");
            valid = false;
        }
    
        // Validation for Book ID
        if (bookIdText.isEmpty()) {  // check for placeholder
            errorMessage.append("- Please enter book ID.\n");
            valid = false;
        } else if (!bookIdText.matches(bookIdPattern)) {
            errorMessage.append("- Book ID must be in the format BK0001.\n");
            valid = false;
        }
        for(ReserveBookDetail reserveBook:BookFileManager.getReserveBookList())
        {
            LocalDate reservationDate = reserveBook.getReservationBookDate();
            LocalDate today = LocalDate.now();
            LocalDate sevenDaysLater = today.plusDays(6);

            // Check if book is reserved for any day between tomorrow and 7 days from today
            if (reserveBook.getBook().getBookID().equals(bookIdText)
                    && (!reservationDate.isBefore(today) && !reservationDate.isAfter(sevenDaysLater))) {
                errorMessage.append("- Book is already reserved in the next 7 days.\n");
                valid = false;
                break;
            }
        }        
        
        for (Book book : BookFileManager.getBookList()) {
            if (book.getBookID().equals(bookIdText)) {
                System.out.println(book.isAvailable());
                if (!book.isAvailable()) { // If book is unavailable
                    errorMessage.append("- This book is currently unavailable.\n");
                    valid = false;
                    break; 
                }
            }
        }    
        
        // Show error dialog if not valid
        if (!valid) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } else {
            
         
            LocalDate returnDate=null;
            int count=0;
            for(Book book:BookFileManager.getBookList()){
                
                    

                if(bookIdField.getText().equals(book.getBookID())){
                    String image=BookFileManager.getBookImageList().get(count);
                    String borrowBookStatus="Borrowed";
                    String borrowMethod="Physical";

                    String borrowId="BR0001";
                    for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
                        borrowId=borrow.generateNextId();
                    }

                    for (UserData user : users) {
                        
                        if (userIdText.equals(user.getUserid())) {
                            BorrowDetail newBorrow = new BorrowDetail(borrowId,user,book, borrowDate,returnDate,borrowBookStatus,borrowMethod,image);
                            borrowedList.add(newBorrow);
                            break;
                        }
                    }
                
                    book.setAvailability(false);
                }
                count++;
            }
            
            
         
        

            
            JOptionPane.showMessageDialog(this,
                    "Borrowing successful:\nUserID: " + userIdField.getText() +
                            "\nBook: " + bookIdField.getText() +
                            "\nBorrow Date: " + borrowDate.format(FORMATTER) +
                            "\nReturn Date: " + dueDate.format(FORMATTER),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        BookFileManager.updateFile(BookFileManager.getBorrowFile());
        BookFileManager.updateFile(BookFileManager.getBookFile());

            
    }

            
 
    

}

class ReturnBookForm extends JFrame {
    private JTextField userIdField,bookIdField;
    private LocalDate returnDate;
    private static List<BorrowDetail> borrowedlist=new ArrayList<>();;
    private DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JComponent[] returnComponents;
    private Book book;

    public ReturnBookForm(StaffData currentStaff) {
        setTitle("Staff Return Book Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizes the window
        setUndecorated(false); // Set to true if you want to remove the title bar (optional)


        

        borrowedlist=BookFileManager.getBorrowedList();
        
        // Fonts
        Font labelFont = new Font("SimSun", Font.PLAIN, 22); // Larger font for labels
        Font fieldFont = new Font("SimSun", Font.PLAIN, 20); // Larger font for input fields
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Increased padding around panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15); // Add more space between components

        // Title
        JLabel title = new JLabel("Staff Return Book Page", SwingConstants.CENTER);
        title.setFont(new Font("SimSun", Font.BOLD, 28)); // Larger font for title
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // userId
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(labelFont);
        panel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        userIdField = new JTextField();
        userIdField.setFont(fieldFont);
        addSmartPlaceholder(userIdField, "ex:U0001");
        userIdField.setPreferredSize(new Dimension(350, 40)); // Increased size of the text field
        panel.add(userIdField, gbc);

        


        // Book ID
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel bookLabel = new JLabel("Book ID:");
        bookLabel.setFont(labelFont);
        panel.add(bookLabel, gbc);

        
        gbc.gridx = 1;
        bookIdField = new JTextField();
        bookIdField.setFont(fieldFont);
        addSmartPlaceholder(bookIdField, "ex:BK0001");
        bookIdField.setPreferredSize(new Dimension(350, 40)); // Increased size of the text field
        panel.add(bookIdField, gbc);

        
        returnComponents = new JComponent[] {
            userIdField,
            bookIdField
        };

        for (int i = 0; i < returnComponents.length; i++) {
            int index = i;
            JTextField field = (JTextField) returnComponents[i];
            field.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN && index < returnComponents.length - 1) {
                        returnComponents[index + 1].requestFocus();
                    } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP && index > 0) {
                        returnComponents[index - 1].requestFocus();
                    }
                }
            });
            field.addActionListener(e -> validateForm()); // Pressing Enter triggers validateForm()

        }


        


        // Return Date
        gbc.gridx = 0;
        gbc.gridy++;
        
        JLabel borrowLabel = new JLabel("Return Date:");
        borrowLabel.setFont(labelFont);
        panel.add(borrowLabel, gbc);

        

        gbc.gridx = 1;
        returnDate= LocalDate.now();
        String formattedborrowDate = returnDate.format(FORMATTER);
        JLabel borrowDateValueLabel = new JLabel(formattedborrowDate);
        borrowDateValueLabel.setFont(fieldFont);
        borrowDateValueLabel.setPreferredSize(new Dimension(350, 50)); // Optional styling
        panel.add(borrowDateValueLabel, gbc);

        

        // Submit button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton returnButton = new JButton("Return Books");
        returnButton.setFont(new Font("SimSun", Font.BOLD, 24)); // Larger font for button text
        returnButton.setPreferredSize(new Dimension(300, 70)); // Increased size of the button
        returnButton.setBackground(new Color(76, 175, 80));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> validateForm());
        panel.add(returnButton, gbc);

        

        add(panel);

        getRootPane().setDefaultButton(returnButton);

        setVisible(true); 
    }

    public static void addSmartPlaceholder(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
    
        final boolean[] ISPLACEHOLDERACTIVE = {true};
    
        // When user types something
        textField.addKeyListener(new java.awt.event.KeyAdapter() {
            
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (ISPLACEHOLDERACTIVE[0]) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    ISPLACEHOLDERACTIVE[0] = false;
                }
            }
        });
    
        // When user clicks away or comes back
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                    ISPLACEHOLDERACTIVE[0] = true;
                }
            }
    
          
            public void focusGained(java.awt.event.FocusEvent e) {
                if (ISPLACEHOLDERACTIVE[0]) {
                    // Put cursor at beginning if still placeholder
                    textField.setCaretPosition(0);
                }
            }
        });
    

    }

    private void validateForm() {
        
        boolean valid = true;
        StringBuilder errorMessage = new StringBuilder();
        String bookIdPattern = "(?i)^BK\\d{4}$";  // e.g. BK0001
        String userIdPattern = "(?i)^U\\d{4}$";   // e.g. U0001
        String userIdText = userIdField.getText().trim();
        String bookIdText = bookIdField.getText().trim();

        List<BlackListDetail> blacklist = BlackList.loadBlacklist("Blacklist.txt"); // Adjust filename if needed
        for (BlackListDetail detail : blacklist) {
            if (detail.getUser().getUserid().equalsIgnoreCase(userIdText) && detail.isBlacklisted()) {
                errorMessage.append("- User is blacklisted and cannot perform this action.\n");
                valid = false;
                break;
            }
        }
        
        // Validation
        if (userIdText.isEmpty()) {  // check for placeholder
            errorMessage.append("- Please enter user ID.\n");
            valid = false;
        } else if (!userIdText.matches(userIdPattern)) {
            errorMessage.append("- User ID must be in the format U0001.\n");
        }else {
            
            boolean userExists = false;
            ArrayList<UserData> userList = new ArrayList<>();
            userList=UserFileManager.loadAllUsers();
            for (UserData user : userList) {
                if (user.getUserid().equals(userIdText)) {
                    userExists = true;
                    break;
                }
            }

            if (!userExists) {
                errorMessage.append("- User ID does not exist.\n");
                valid = false;
            }
        }
    
        // Validation for Book ID
        if (bookIdText.isEmpty()) {  // Check if bookId is empty
            errorMessage.append("- Please enter book ID.\n");
            valid = false;
        } else if (!bookIdText.matches(bookIdPattern)) {  // Check if bookId matches the required pattern
            errorMessage.append("- Book ID must be in the format BK0001.\n");
            valid = false;
        } else {
            // Check if the book exists in the book list
            book = null;
            for (Book books : BookFileManager.getBookList()) {
                if (books.getBookID().equalsIgnoreCase(bookIdText)) {
                    book = books;
                    break;
                }
            }
            
            if (book == null) {
                errorMessage.append("- Book ID does not exist.\n");
                valid = false;
            } 
            boolean isBorrowedByUser = false;
            boolean isBorrowedByOthers = false;

            for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
                if (borrow.getBook().equals(book)
                        && borrow.getUser().getUserid().equalsIgnoreCase(userIdText)
                        && borrow.getStatus().equalsIgnoreCase("Borrowed")) {
                    isBorrowedByUser = true;
                    break;
                }
            }

            if (!isBorrowedByUser) {
                errorMessage.append("- This book is not currently borrowed by user ID: " + userIdText + ".\n");
                valid = false;
            }
            
        }
    
        // Show error dialog if not valid
        if (!valid) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Proceed with success dialog
           
            for (BorrowDetail borrow : BookFileManager.getBorrowedList()) {
                String userIdTexts = borrow.getUser().getUserid();  // or however you store it
                String bookIdTexts = borrow.getBook().getBookID();  // likewise
                boolean isCurrentUserBook = borrow.getBook().equals(book)
                && borrow.getUser().getUserid().equalsIgnoreCase(userIdText)
                && borrow.getStatus().equalsIgnoreCase("Borrowed");
                LocalDate today=LocalDate.now();
                if(isCurrentUserBook){
                    if(!today.isBefore(borrow.getDueDate())){
                        OverdueChecker checker = new OverdueChecker();
                        boolean fine=checker.checkOverdueAndShowFine(borrow, userIdTexts, bookIdTexts); // Waits here for user input

                        
                        
                        
                        
                        
                        
                        
                    }
                    if (borrow.getBook().equals(book)) {
                        borrow.setReturnDate(LocalDate.now());  // Set the return date
                        if(borrow.getReturnDate().isAfter(borrow.getDueDate())){
                            borrow.setStatus("Overdue");
                        }else{
                            borrow.setStatus("Returned");
                        
                        }
                        book.setAvailability(true);  // Mark the book as available
                        
                    }
                    
                }
                
                
            }

            
      
            BookFileManager.updateFile(BookFileManager.getBorrowFile()); // Make sure to call the method after updating the return date
            BookFileManager.updateFile(BookFileManager.getBookFile()); // Make sure to call the method after updating the return date

            
        

            
            JOptionPane.showMessageDialog(this,
        "Return book successful:\nUserID: " + userIdField.getText() +
                "\nBook: " + bookIdField.getText() +
                "\nReturn Date: " + returnDate.format(FORMATTER),
        "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

   
    

    
}

class AddNewBook extends JFrame {
    private JTextField bookIDField, titleField, authorField, genreField, isbnField, publisherField, languageField, pagesField, yearField;
    private JButton submitButton,resetButton;
    
    private JButton uploadButton;
    private JLabel fileLabel; // to show selected file name (optional)
    private File selectedFile;
    private Font fieldFont = new Font("SimSun", Font.PLAIN, 20);
    
    private JComponent[] newBookComponents;

    private static final String FILE_PATH = "Book.txt";
    private List<String> bookimagelist;
    private String fileName;
    private boolean isfileSelect=false;
    private String bookId;
    public AddNewBook(StaffData currentStaff) {
        bookimagelist=BookFileManager.getBookImageList();
        setTitle("Add New Book");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

       
        setLayout(new BorderLayout());
        
        JLabel headerLabel = new JLabel("Add New Book", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);
        
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setPreferredSize(new Dimension(800, 600)); 
        filterPanel.setMaximumSize(new Dimension(800, 600));
        filterPanel.setMinimumSize(new Dimension(800, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 2, 5, 2); 

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0; 
        


    

        bookIDField = createTextField();
        titleField = createTextField();
        authorField = createTextField();
        genreField = createTextField();
        isbnField = createTextField();
        addPlaceholderText(isbnField, "ex:9780306406157 or 0306406152");

        publisherField = createTextField();
        languageField = createTextField();
        pagesField = createTextField();
        yearField = createTextField();


        

        newBookComponents = new JComponent[] {
            
            bookIDField,
            titleField,
            authorField,
            genreField,
            isbnField,
            publisherField,
            languageField,
            pagesField,
            yearField
        
           
         
        };

        for (int i = 0; i < newBookComponents.length; i++) {
            int index = i;
            newBookComponents[i].addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN && index < newBookComponents.length - 1) {
                        newBookComponents[index + 1].requestFocus();
                    } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP && index > 0) {
                        newBookComponents[index - 1].requestFocus();
                    }
                }
            });
        }

      
        submitButton = new JButton("Submit");
        resetButton = new JButton("Reset");
       
        submitButton.setFont(fieldFont);
        resetButton.setFont(fieldFont);

        
        addRow(filterPanel, gbc, 1, "Book ID:", bookIDField);
        addRow(filterPanel, gbc, 2, "Title:", titleField);
        addRow(filterPanel, gbc, 3, "Author:", authorField);
        addRow(filterPanel, gbc, 4, "Genre:", genreField);
        addRow(filterPanel, gbc, 5, "ISBN:", isbnField);
        addRow(filterPanel, gbc, 6, "Publisher:", publisherField);
        addRow(filterPanel, gbc, 7, "Language:", languageField);
        addRow(filterPanel, gbc, 8, "Pages:", pagesField);
        addRow(filterPanel, gbc, 9, "Year:", yearField);
        for(Book book:BookFileManager.getBookList()){
            bookId=book.generateNextBookID();
        }
        bookIDField.setText(bookId);
        bookIDField.setEditable(false);


        

        JPanel navigatePanel = new JPanel(new GridBagLayout());
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 6;
        navigatePanel.add(submitButton, gbc);

        gbc.gridx = 1;
        navigatePanel.add(resetButton, gbc);
        
        
        
        

        // Upload file components
        uploadButton = new JButton("Choose File");
        uploadButton.setFont(fieldFont);
        fileLabel = new JLabel();
        fileLabel.setText("No file chosen");
        fileLabel.setFont(new Font("SimSun", Font.ITALIC, 14));
        fileLabel.setPreferredSize(new Dimension(300, 80)); // consistent width and height

        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel uploadFile=new JLabel("Upload File:");
        filterPanel.add(uploadFile, gbc);

        gbc.gridx = 1;
        filterPanel.add(uploadButton, gbc);

       
        gbc.gridy = 11;
        filterPanel.add(fileLabel, gbc);


        
        
        add(filterPanel, BorderLayout.CENTER);
        
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        filterPanel.add(navigatePanel, gbc);
        gbc.gridwidth = 1;



        submitButton.addActionListener(e -> saveBook());

        resetButton.addActionListener(e -> clearFields());

        getRootPane().setDefaultButton(submitButton);


        uploadButton.addActionListener(e -> {
            
            
            // Step 2: Initialize JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Book Image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPG Images (*.jpg)", "jpg");

            
            fileChooser.addChoosableFileFilter(jpgFilter);

            
            fileChooser.setAcceptAllFileFilterUsed(true);
            
            int number=0;
            int countnumber=0;

            if(bookimagelist.size()==0){
                number=1;
            }
            for(String filePath:bookimagelist){
                
                Pattern pattern = Pattern.compile("_(\\d+)");
                Matcher matcher = pattern.matcher(filePath);

                if (matcher.find()) {
                    // Extract the number found after the underscore
                    String numberString = matcher.group(1);
                    number=Integer.parseInt(numberString);
                    countnumber=Math.max(number,countnumber);  //

                }

            }
            countnumber++;
            // Step 3: Set default file name to "book_cover.jpg"
            fileName = "bookCover_"+countnumber+".jpg";
            
            
            
            
            
        
            fileChooser.setApproveButtonText("Upload");

            
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                // Get the selected file
                selectedFile = fileChooser.getSelectedFile();
                String selectedFilePath = selectedFile.getAbsolutePath();
                fileLabel.setText("<html><br>Selected file:<br>" + breakLongPath(selectedFilePath, 50) + "</html>");
                isfileSelect=true;
                
            }
            Font customFont = new Font("SansSerif", Font.BOLD, 16); // Change font and size

            SwingUtilities.invokeLater(() -> {
                setFontForAllComponents(fileChooser, customFont);
            });
        });

       

        // Maximize the window
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        
        

        setVisible(true);
    }

    private void addPlaceholderText(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
    
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
    
    private String breakLongPath(String path, int maxLineLength) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        while (index < path.length()) {
            int endIndex = Math.min(index + maxLineLength, path.length());
            builder.append(path, index, endIndex).append("<br>");
            index = endIndex;
        }
        return builder.toString();
    }

    private static void copyFile(File source, File destination) throws IOException {
        try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(destination)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }
    
    private void setFontForAllComponents(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setFontForAllComponents(child, font);
            }
        }
    }
    


    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
        // Create label and set font
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(fieldFont);
        
        
        jLabel.setPreferredSize(new Dimension(120, 45)); 
        jLabel.setMaximumSize(new Dimension(120, 45));  
    
        // Configure the component (TextField in this case)
        component.setFont(fieldFont);
        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setMinimumSize(new Dimension(250, 45));  
        }
    
        // Adding the label to the panel at the specified grid position
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(jLabel, gbc);
    
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    
    

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(fieldFont);
        textField.setPreferredSize(new Dimension(350, 45)); // Fixed width and height
        textField.setMaximumSize(new Dimension(350, 45));  // Ensures it doesn't grow
        textField.setMinimumSize(new Dimension(350, 45));  // Ensures it doesn't shrink
        return textField;
    }

    private void saveBook() {
        try {

            if (!validateInputs()) {
                
                return; 
            }

            if(!isfileSelect){
                
                JOptionPane.showMessageDialog(this, "Please upload book cover" );
        
                return;
            }
            File predefinedFolder = new File(BookFileManager.getBookImageFile()); // Modify this path to your desired folder
            
            String bookID = bookIDField.getText().trim();
            
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            String isbn = isbnField.getText().trim();
            String publisher = publisherField.getText().trim();
            String language = languageField.getText().trim();
            String pagesString = pagesField.getText().trim();
            String yeaString = yearField.getText().trim();
            
            boolean availability = true;

            int pages = Integer.parseInt(pagesString);
            int year = Integer.parseInt(yeaString);

            Book newBook = new Book(bookID, title, author, isbn, genre, publisher, language,pages, year, availability);
            BookFileManager.getBookList().add(newBook);
            
            File destinationFile = new File(predefinedFolder, fileName);
            try {
                copyFile(selectedFile, destinationFile);
                fileLabel.setText("File saved as: " + destinationFile.getAbsolutePath());
                bookimagelist.add(fileName);
            } catch (IOException ex) {
                fileLabel.setText("Error saving file.");
                ex.printStackTrace();
            }
            File file = new File(FILE_PATH);
            boolean isNewFile = !file.exists() || file.length() == 0;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                if (isNewFile) {
                    writer.write("BookID\tTitle\tAuthor\tISBN\tGenre\tPublisher\tLanguage\tPages\tYear\tAvailability");
                    writer.newLine();
                }

                for (Book book : BookFileManager.getBookList()) {
                    if (book.getBookID().equals(bookID)){
                        writer.write(book.toString());
                    
                        writer.newLine(); // Add a new line after each borrow entry
                    }
                   
                }
                
                
            }

            JOptionPane.showMessageDialog(this, "Book added successfully!");
            clearFields();
            bookIDField.setText("");  // First remove any existing text
            for(Book book:BookFileManager.getBookList()){
                bookId=book.generateNextBookID();
            }
            bookIDField.setText(bookId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving book: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean validateInputs() {
        // Get trimmed values
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = genreField.getText().trim();
        String isbn = isbnField.getText().trim();
        String publisher = publisherField.getText().trim();
        String language = languageField.getText().trim();
        String pagesText = pagesField.getText().trim();
        String yearText = yearField.getText().trim();
    
        // Check for empty fields
        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || isbn.isEmpty() ||
            publisher.isEmpty() || language.isEmpty() || pagesText.isEmpty() || yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        
    
        // Check numeric fields
        if (!pagesText.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Pages must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    
        if (!yearText.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "Year must be a 4-digit number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    
        // Optional: check for special characters in name fields
        if (!title.matches("[\\u4e00-\\u9fffa-zA-Z0-9 .,:'\"!?()-]+") ||
            !author.matches("[\\u4e00-\\u9fffa-zA-Z .'-]+") ||
            !publisher.matches("[\\u4e00-\\u9fffa-zA-Z0-9 .'-]+")) {
            JOptionPane.showMessageDialog(this, "Please remove special characters from Title, Author, or Publisher fields.", "Invalid Characters", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String isbn10Pattern = "^[0-9]{9}[0-9X]$";
        String isbn13Pattern = "^[0-9]{13}$";

        String isbnText=isbnField.getText();

        if (isbnText.matches(isbn10Pattern)||isbnText.matches(isbn13Pattern)) {
            return true;
        }else{
            JOptionPane.showMessageDialog(this, "Please follow the isbn format,either ISBN-10(0306406152) or ISBN-13(9780306406157)", "Invalid ISBN format", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    
        
    }
    

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        genreField.setText("");
        isbnField.setText("");
        publisherField.setText("");
        languageField.setText("");
        pagesField.setText("");
        yearField.setText("");
        fileLabel.setText("No file chosen");
        isfileSelect=false;
    }

    

}





class StaffReserveBookManagement extends JFrame {

    private static List<ReserveBookDetail> reserveBookList = new ArrayList<>();
    private DefaultTableModel model;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private JTable table;
    private List<ReserveBookDetail> filteredReserved = new ArrayList<>();

    private JDialog calendarDialog;
    private JTextField searchField;
    private JComboBox<String> bookReservationStatusComboBox;
    private JLabel monthLabel;
    private JComboBox<Integer> yearComboBox;
    private JPanel calendarPanel;
    private Calendar calendar;

    private String selectedDateType = "";
    private LocalDate selectedReservationMadeDate, selectedReservationBookDate;

    private JLabel reservationMadeDateLabel;
    private JLabel reservationBookDateLabel;

    private int row;

    public StaffReserveBookManagement(StaffData currentStaff) {
        setTitle("Staff Reserve Book Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(900, 900);
        setLocationRelativeTo(null);

        reserveBookList = BookFileManager.getReserveBookList();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        JLabel title = new JLabel("Staff Reserve Book Page", SwingConstants.CENTER);
        title.setFont(new Font("SimSun", Font.BOLD, 28));
        JLabel tableHeader = new JLabel("Reserved Books List", SwingConstants.CENTER);
        tableHeader.setFont(new Font("SimSun", Font.PLAIN, 20));
        topPanel.add(title);
        topPanel.add(tableHeader);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton advancedFunctionButton = new JButton("Advanced function");
        advancedFunctionButton.addActionListener(e -> advancedFunction());
        filterPanel.add(new JLabel("Enter search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);
        filterPanel.add(advancedFunctionButton);

        JButton resetButton = new JButton("Reset");
        filterPanel.add(resetButton); 
        resetButton.addActionListener(e -> resetFunction());

        String[] columnNames = {
            "Reserve Book ID", "BookName", "User", "Phone", 
            "Reservation Made Date", "Reservation Book Date", 
            "Book Reservation Status", "Button"
        };
        model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        table = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        table.setFont(new Font("SimSun", Font.PLAIN, 16));
        table.setRowHeight(80);

        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaRenderer());

        // Set preferred column widths
        int[] widths = {120, 250, 100, 100, 130, 130, 150, 220};
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
        }

        table.getColumn("Button").setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton checkOutButton = new JButton("Check Out");
                JButton cancelButton = new JButton("Cancel");
                int modelRow = table.convertRowIndexToModel(row);
                ReserveBookDetail reserve = reserveBookList.get(modelRow);
                boolean isEnabled = reserve.getStatus().equals("Reserved");
                checkOutButton.setEnabled(isEnabled);
                cancelButton.setEnabled(isEnabled);
                panel.add(checkOutButton);
                panel.add(cancelButton);
                return panel;
            }
        });

        table.getColumn("Button").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private JPanel panel;
            private JButton checkOutButton;
            private JButton cancelButton;

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                checkOutButton = new JButton("Check Out");
                cancelButton = new JButton("Cancel");
                int modelRow = table.convertRowIndexToModel(row);
                ReserveBookDetail reserve = reserveBookList.get(modelRow);
                boolean isEnabled = reserve.getStatus().equals("Reserved");
                checkOutButton.setEnabled(isEnabled);
                cancelButton.setEnabled(isEnabled);
                panel.add(checkOutButton);
                panel.add(cancelButton);

                checkOutButton.addActionListener(e -> {
                    if (reserve.getReservationBookDate().isEqual(LocalDate.now())) {
                        reserve.setStatus("Borrowed");
                        model.setValueAt("Borrowed", modelRow, 6);
                        BookFileManager.updateFile(BookFileManager.getReserveBookFile());
                        fireEditingStopped();
                    } else {
                        JOptionPane.showMessageDialog(null, "Today is not the reservation date.");
                        fireEditingStopped();
                    }
                });

                cancelButton.addActionListener(e -> {
                    reserve.setStatus("Cancelled");
                    model.setValueAt("Cancelled", modelRow, 6);
                    BookFileManager.updateFile(BookFileManager.getReserveBookFile());
                    fireEditingStopped();
                });

                return panel;
            }

            public Object getCellEditorValue() {
                return "Button";
            }
        });

        searchButton.addActionListener(e -> {
            normalSearch(searchField.getText().toLowerCase());
            loadTableData(filteredReserved);
        });

        searchField.addActionListener(e -> {
            normalSearch(searchField.getText().toLowerCase());
            loadTableData(filteredReserved);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        showExpiredReservation();
        loadTableData(reserveBookList);
    }

    public void resetFunction(){
        searchField.setText(""); // Clear search field
        filteredReserved = new ArrayList<>(reserveBookList); // Reset the filtered list to original
        loadTableData(filteredReserved); // Reload the full data into the table
    }

    public void showExpiredReservation() {
        StringBuilder expiredMessages = new StringBuilder();
        for (ReserveBookDetail reserve : reserveBookList) {
            if ("Expired".equalsIgnoreCase(reserve.getStatus())) {
                expiredMessages.append("Reservation ")
                               .append(reserve.getId())
                               .append(" is expired.\n");
            }
        }

        if (expiredMessages.length() > 0) {
            JTextArea textArea = new JTextArea(expiredMessages.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300)); // adjust as needed

            JOptionPane.showMessageDialog(null, scrollPane, "Expired Reservations", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void advancedFunction() {
        JDialog dialog = new JDialog(this, "Advanced Filters", true);
        dialog.setSize(550, 300);
        dialog.setLocationRelativeTo(this); // center on the parent window

        JPanel advancedFilterPanel = new JPanel();
        advancedFilterPanel.setLayout(new GridLayout(5, 2, 10, 10));

        advancedFilterPanel.add(new JLabel("Book Reservation Status:"));
        bookReservationStatusComboBox = new JComboBox<>(new String[]{"All", "Reserved", "Borrowed","Cancelled","Expired"});
        advancedFilterPanel.add(bookReservationStatusComboBox);

        advancedFilterPanel.add(new JLabel("Reservation Made Date:"));
        JPanel reservationMadeDatePanel = new JPanel(new BorderLayout());
        JButton reservationMadeDateButton = new JButton("Select Reservation Made Date");
        reservationMadeDateLabel = new JLabel("No date selected");
        reservationMadeDatePanel.add(reservationMadeDateButton, BorderLayout.NORTH);
        reservationMadeDatePanel.add(reservationMadeDateLabel, BorderLayout.CENTER);
        advancedFilterPanel.add(reservationMadeDatePanel);

        advancedFilterPanel.add(new JLabel("Reservation Book Date:"));
        JPanel reservationBookDatePanel = new JPanel(new BorderLayout());
        JButton reservationBookDateButton = new JButton("Select Reservation Book Date");
        reservationBookDateLabel = new JLabel("No date selected");
        reservationBookDatePanel.add(reservationBookDateButton, BorderLayout.NORTH);
        reservationBookDatePanel.add(reservationBookDateLabel, BorderLayout.CENTER);
        advancedFilterPanel.add(reservationBookDatePanel);

        reservationMadeDateButton.addActionListener(e -> {
            selectedDateType = "reservationMadeDate";
            openCalendarGUI();
        });

        reservationBookDateButton.addActionListener(e -> {
            selectedDateType = "reservationBookDate";
            openCalendarGUI();
        });

        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> {
            applyAdvancedFilters();
            loadTableData(filteredReserved);
            dialog.dispose(); // Close the dialog after applying filters
        });
        advancedFilterPanel.add(new JLabel());
        advancedFilterPanel.add(applyFilterButton);

        dialog.add(advancedFilterPanel);
        dialog.setVisible(true);
    }

    public void openCalendarGUI() {
        filterReserveBookCalendar();
    }

    private void filterReserveBookCalendar() {
        calendarDialog = new JDialog(this, "Select Date", true);
        calendarDialog.setSize(700, 500);
        calendarDialog.setLocationRelativeTo(this); // Center the dialog
    
        // Use a panel to hold everything inside the dialog
        JPanel contentPanel = new JPanel(new BorderLayout());
    
        calendar = Calendar.getInstance();  // default date and time
        monthLabel = new JLabel("", JLabel.CENTER);
    
        JButton calenderprevButton = new JButton("<");
        JButton calendernextButton = new JButton(">");
    
        calenderprevButton.addActionListener(e -> changeMonth(-1));
        calendernextButton.addActionListener(e -> changeMonth(1));
    
        JPanel calendertopPanel = new JPanel(new BorderLayout());
        JPanel calendernavPanel = new JPanel();
        calendernavPanel.add(calenderprevButton);
        calendernavPanel.add(monthLabel);
        calendernavPanel.add(calendernextButton);
    
        yearComboBox = new JComboBox<>();
        for (int i = 1990; i <= 2050; i++) {
            yearComboBox.addItem(i);
        }
        yearComboBox.setSelectedItem(calendar.get(Calendar.YEAR));
        yearComboBox.addActionListener(e -> updateCalendar());
    
        JPanel yearPanel = new JPanel();
        yearPanel.add(new JLabel("Change year:"));
        yearPanel.add(yearComboBox);
    
        calendarPanel = new JPanel(new GridLayout(0, 7));
    
        calendertopPanel.add(calendernavPanel, BorderLayout.CENTER);
        contentPanel.add(calendertopPanel, BorderLayout.NORTH);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(yearPanel, BorderLayout.SOUTH);
    
        // Set the content panel as the dialog's content pane
        calendarDialog.setContentPane(contentPanel);
    
        updateCalendar(); // update your calendar grid
    
        calendarDialog.setVisible(true); // show the dialog
    }

    
    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta); //modify current month,then if delta is positive,then add month else minus month
        updateCalendar();
    }

    private void updateCalendar() {
        calendar.set(Calendar.YEAR, (int) yearComboBox.getSelectedItem());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int month = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] headers = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Satursday"};

        calendarPanel.removeAll();
        for (String header : headers) {
            calendarPanel.add(new JLabel(header, JLabel.CENTER));
        }

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel("")); //empty 
        }

        for (int day = 1; day <= daysInMonth; day++) {
            int currentDay=day;
            JLabel dayLabel=new JLabel(String.valueOf(currentDay), JLabel.CENTER);
            calendarPanel.add(dayLabel);

            dayLabel.setForeground(Color.BLUE);           // Text color
            dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

            
            dayLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String monthsString = String.valueOf(month + 1);
                    String daysString = String.valueOf(currentDay);
                    if (monthsString.length() == 1) {
                        monthsString = "0" + monthsString;
                    }
                    if (daysString.length() == 1) {
                        daysString = "0" + daysString;
                    }
                    String dateString = daysString + "/" + monthsString + "/" + yearComboBox.getSelectedItem().toString();
                    
                    LocalDate selectedDate = LocalDate.parse(dateString, FORMATTER);
        
                    int option = JOptionPane.showConfirmDialog(
                        null,
                        "You clicked on day: " + selectedDate.format(FORMATTER),
                        "Confirm Date",
                        JOptionPane.YES_NO_OPTION
                    );
        
                    if (option == JOptionPane.YES_OPTION) {
                        
                    
                        String displayDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    
                        
                        switch (selectedDateType) {
                            case "reservationMadeDate":
                                selectedReservationMadeDate = selectedDate;
                                reservationMadeDateLabel.setText("Reservation Made Date: " + displayDate);
                                break;
                            case "reservationBookDate":
                                selectedReservationBookDate = selectedDate;
                                reservationBookDateLabel.setText("Reservation Book Date: " + displayDate);
                                break;
                            
                        }
                    
                        calendarDialog.dispose();
                    }
                    
                    
                }
                
            });
        }

        

        calendarPanel.revalidate();
        calendarPanel.repaint();

        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()));
    }

    

    private void applyAdvancedFilters() {
        filteredReserved.clear();

        String bookReservationStatus = (String) bookReservationStatusComboBox.getSelectedItem();

        for (ReserveBookDetail reserve : reserveBookList) {
            boolean match = false;

            // Filter by Book Reservation Status
            if (bookReservationStatus != null && !bookReservationStatus.equals("All") &&
                reserve.getStatus().equalsIgnoreCase(bookReservationStatus)) {
                match = true;
            }

            // Filter by Reservation Made Date
            if (selectedReservationMadeDate != null && 
                reserve.getReservationMadeDate().isEqual(selectedReservationMadeDate)) {
                match = true;
            }

            // Filter by Reservation Book Date
            if (selectedReservationBookDate != null &&
                reserve.getReservationBookDate().isEqual(selectedReservationBookDate)) {
                match = true;
            }

            if (match) {
                filteredReserved.add(reserve);
            }
        }
    }

    private void normalSearch(String query) {
        filteredReserved.clear();
        for (ReserveBookDetail reserve : reserveBookList) {
            if (reserve.toString().toLowerCase().contains(query)) {
                filteredReserved.add(reserve);
            }
        }
    }

    private void loadTableData(List<ReserveBookDetail> filteredReserved) {
        model.setRowCount(0); // Clear existing data

        for (ReserveBookDetail reserve : filteredReserved) {
            Object[] row = new Object[]{
                reserve.getId(), reserve.getBook().getBookName(), reserve.getUser().getName(),
                reserve.getUser().getPhoneNumber(), reserve.getReservationMadeDate().format(FORMATTER),
                reserve.getReservationBookDate().format(FORMATTER),
                reserve.getStatus(), "Button"
            };
            model.addRow(row);
        }
    }
}
class TextAreaRenderer extends JTextArea implements TableCellRenderer {
    public TextAreaRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText(value == null ? "" : value.toString());
        setFont(table.getFont());
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        return this;
    }
}

class staffBorrowBookManagement extends JFrame {
    private List<BorrowDetail> borrowedList = new ArrayList<>();
    private DefaultTableModel model;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private List<BorrowDetail> filteredBorrow = new ArrayList<>();
    private JDialog calendarDialog;
    // Components for the advanced filter
    private JTextField searchField;
    private JComboBox<String> borrowMethodComboBox;
    private JComboBox<String> bookReservationStatusComboBox;

    private JLabel monthLabel;
    private JComboBox<Integer> yearComboBox;
    private JPanel calendarPanel;
    private Calendar calendar;
    


    private String selectedDateType = ""; 
    private LocalDate selectedBorrowDate, selectedDueDate, selectedReturnDate;

    private JLabel borrowDateLabel;
    private JLabel dueDateLabel;
    private JLabel returnDateLabel;

    
    
    public staffBorrowBookManagement(StaffData currentStaff) {
        setTitle("Staff Borrow Book Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        borrowedList = BookFileManager.getBorrowedList();

       

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top section
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        JLabel title = new JLabel("Staff Borrow Book Page", SwingConstants.CENTER);
        title.setFont(new Font("SimSun", Font.BOLD, 28));
        JLabel tableHeader = new JLabel("Borrowed Books List", SwingConstants.CENTER);
        tableHeader.setFont(new Font("SimSun", Font.PLAIN, 20));
        topPanel.add(title);
        topPanel.add(tableHeader);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Search Panel (Basic search)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Enter search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton); 
        

        JButton advancedFunctionButton = new JButton("Advanced function");
        advancedFunctionButton.addActionListener(e -> advancedFunction());
        filterPanel.add(advancedFunctionButton);

        JButton resetButton = new JButton("Reset");
        filterPanel.add(resetButton); 
        resetButton.addActionListener(e -> resetFunction());

        // Table
        String[] columnNames = {"BorrowId","BookId", "BookName", "BorrowDate", "Borrower", "Phone Number", "Due Date", "Return Date", "Borrow Book Status","Borrow Method"};
        model = new DefaultTableModel(columnNames, 0){
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        

        JTable table = new JTable(model);
        table.setFont(new Font("SimSun", Font.PLAIN, 16));
        table.setRowHeight(80);


        table.getColumnModel().getColumn(2).setCellRenderer(new TextAreaRenderer());

        // Set preferred column widths
        int[] widths = {120,120, 250, 100, 100, 130, 130, 150, 220};
        for (int i = 0; i < widths.length; i++) {
            TableColumn col = table.getColumnModel().getColumn(i);
            col.setPreferredWidth(widths[i]);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Action listeners
        searchButton.addActionListener((ActionEvent e) -> {
            String query = searchField.getText().toLowerCase();
            normalSearch(query);
            loadTableData(filteredBorrow);
        });

        searchField.addActionListener((ActionEvent e) -> {
            String query = searchField.getText().toLowerCase();
            normalSearch(query);
            loadTableData(filteredBorrow);
        });

        

        // Layout
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);


        // Maximize the window
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        loadTableData(borrowedList); // Initial load
    }

    public void resetFunction() {
        searchField.setText(""); // Clear search field
        selectedBorrowDate = null;
        selectedDueDate = null;
        selectedReturnDate = null;
    
        if (borrowDateLabel != null) borrowDateLabel.setText("No date selected");
        if (dueDateLabel != null) dueDateLabel.setText("No date selected");
        if (returnDateLabel != null) returnDateLabel.setText("No date selected");
    
        if (borrowMethodComboBox != null) borrowMethodComboBox.setSelectedIndex(0);
        if (bookReservationStatusComboBox != null) bookReservationStatusComboBox.setSelectedIndex(0);
    
        filteredBorrow = new ArrayList<>(borrowedList); 
        loadTableData(filteredBorrow); 
    }
    

    public void advancedFunction() {
        // Create a modal dialog
        JDialog dialog = new JDialog(this, "Advanced Filters", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this); // center on the parent window
    
        // Panel setup
        JPanel advancedFilterPanel = new JPanel();
        advancedFilterPanel.setLayout(new GridLayout(6, 2, 10, 10));
        
        // Borrow Method Filter
        advancedFilterPanel.add(new JLabel("Borrow Method:"));
        borrowMethodComboBox = new JComboBox<>(new String[]{"","All", "Online", "Physical"});
        advancedFilterPanel.add(borrowMethodComboBox);
    
        
        advancedFilterPanel.add(new JLabel("Book Reservation Status:"));
        bookReservationStatusComboBox = new JComboBox<>(new String[]{"","All", "Borrowed", "Returned","Overdue"});
        advancedFilterPanel.add(bookReservationStatusComboBox);
        
        // Borrow Date Filter
        advancedFilterPanel.add(new JLabel("Borrow Date:"));
        JPanel borrowPanel = new JPanel(new BorderLayout());
        JButton borrowDateButton = new JButton("Select Borrow Date");
        borrowDateLabel = new JLabel("No date selected");
        borrowPanel.add(borrowDateButton, BorderLayout.NORTH);
        borrowPanel.add(borrowDateLabel, BorderLayout.CENTER);
        advancedFilterPanel.add(borrowPanel);

        // Due Date Filter
        advancedFilterPanel.add(new JLabel("Due Date:"));
        JPanel duePanel = new JPanel(new BorderLayout());
        JButton dueDateButton = new JButton("Select Due Date");
        dueDateLabel = new JLabel("No date selected");
        duePanel.add(dueDateButton, BorderLayout.NORTH);
        duePanel.add(dueDateLabel, BorderLayout.CENTER);
        advancedFilterPanel.add(duePanel);

        // Return Date Filter
        advancedFilterPanel.add(new JLabel("Return Date:"));
        JPanel returnPanel = new JPanel(new BorderLayout());
        JButton returnDateButton = new JButton("Select Return Date");
        returnDateLabel = new JLabel("No date selected");
        returnPanel.add(returnDateButton, BorderLayout.NORTH);
        returnPanel.add(returnDateLabel, BorderLayout.CENTER);
        advancedFilterPanel.add(returnPanel);


        borrowDateButton.addActionListener(e -> {
            selectedDateType = "borrow";
            openCalendarGUI();
        });
        
        dueDateButton.addActionListener(e -> {
            selectedDateType = "due";
            openCalendarGUI();
        });
        
        returnDateButton.addActionListener(e -> {
            selectedDateType = "return";
            openCalendarGUI();
        });
        
    
        
        // Apply Filter Button
        JButton applyFilterButton = new JButton("Apply Filters");
        applyFilterButton.addActionListener(e -> {
            applyAdvancedFilters();
            loadTableData(filteredBorrow);
            dialog.dispose(); // Close the dialog after applying filters
        });
        advancedFilterPanel.add(new JLabel()); 
        advancedFilterPanel.add(applyFilterButton);
    
        // Add panel to dialog
        dialog.add(advancedFilterPanel);
        dialog.setVisible(true);
    }
    

    public void openCalendarGUI() {
        filterBookCalendar();
    }
    
    private void filterBookCalendar() {
        calendarDialog = new JDialog(this, "Select Date", true);
        calendarDialog.setSize(700, 500);
        calendarDialog.setLocationRelativeTo(this); // Center the dialog
    
        // Use a panel to hold everything inside the dialog
        JPanel contentPanel = new JPanel(new BorderLayout());
    
        calendar = Calendar.getInstance();  // default date and time
        monthLabel = new JLabel("", JLabel.CENTER);
    
        JButton calenderPrevButton = new JButton("<");
        JButton calenderNextButton = new JButton(">");
    
        calenderPrevButton.addActionListener(e -> changeMonth(-1));
        calenderNextButton.addActionListener(e -> changeMonth(1));
    
        JPanel calendertopPanel = new JPanel(new BorderLayout());
        JPanel calendernavPanel = new JPanel();
        calendernavPanel.add(calenderPrevButton);
        calendernavPanel.add(monthLabel);
        calendernavPanel.add(calenderNextButton);
    
        yearComboBox = new JComboBox<>();
        for (int i = 1990; i <= 2050; i++) {
            yearComboBox.addItem(i);
        }
        yearComboBox.setSelectedItem(calendar.get(Calendar.YEAR));
        yearComboBox.addActionListener(e -> updateCalendar());
    
        JPanel yearPanel = new JPanel();
        yearPanel.add(new JLabel("Change year:"));
        yearPanel.add(yearComboBox);
    
        calendarPanel = new JPanel(new GridLayout(0, 7));
    
        calendertopPanel.add(calendernavPanel, BorderLayout.CENTER);
        contentPanel.add(calendertopPanel, BorderLayout.NORTH);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(yearPanel, BorderLayout.SOUTH);
    
        
        calendarDialog.setContentPane(contentPanel);
    
        updateCalendar(); 
    
        calendarDialog.setVisible(true);
    }

    
    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta); 
        updateCalendar();
    }

    private void updateCalendar() {
        calendar.set(Calendar.YEAR, (int) yearComboBox.getSelectedItem());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int month = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] headers = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Satursday"};

        calendarPanel.removeAll();
        for (String header : headers) {
            calendarPanel.add(new JLabel(header, JLabel.CENTER));
        }

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel("")); 
        }

        for (int day = 1; day <= daysInMonth; day++) {
            int currentDay=day;
            JLabel dayLabel=new JLabel(String.valueOf(currentDay), JLabel.CENTER);
            calendarPanel.add(dayLabel);

            dayLabel.setForeground(Color.BLUE);          
            dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            
            dayLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String monthsString = String.valueOf(month + 1);
                    String daysString = String.valueOf(currentDay);
                    if (monthsString.length() == 1) {
                        monthsString = "0" + monthsString;
                    }
                    if (daysString.length() == 1) {
                        daysString = "0" + daysString;
                    }
                    String dateString = daysString + "/" + monthsString + "/" + yearComboBox.getSelectedItem().toString();
            
                    LocalDate selectedDate = LocalDate.parse(dateString, FORMATTER);
            
                    int option = JOptionPane.showConfirmDialog(
                        null,
                        "You clicked on day: " + selectedDate.format(FORMATTER),
                        "Confirm Date",
                        JOptionPane.YES_NO_OPTION
                    );
            
                    if (option == JOptionPane.YES_OPTION) {
                        String displayDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/YYYY"));
            
                        switch (selectedDateType) {
                            case "borrow":
                                selectedBorrowDate = selectedDate;
                                borrowDateLabel.setText("Borrow Date selected: " + displayDate);
                                break;
                            case "due":
                                selectedDueDate = selectedDate;
                                dueDateLabel.setText("Due Date selected: " + displayDate);
                                break;
                            case "return":
                                selectedReturnDate = selectedDate;
                                returnDateLabel.setText("Return Date selected: " + displayDate);
                                break;
                        }
            
                        calendarDialog.dispose();
                    }
                }
            });
            
        }

        

        calendarPanel.revalidate();
        calendarPanel.repaint();

        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()));
    }
    
    

    public void normalSearch(String query) {
        filteredBorrow.clear();
        for (BorrowDetail borrow : borrowedList) {
            if (borrow.toWord().toLowerCase().contains(query)) {
                filteredBorrow.add(borrow);
            }
        }
    }

    private void applyAdvancedFilters() {
        filteredBorrow.clear();
    
        for (BorrowDetail borrow : borrowedList) {
            boolean match = true;
    
            String borrowMethod = (String) borrowMethodComboBox.getSelectedItem();
            if (!borrowMethod.isEmpty() && !borrowMethod.equals("All")) {
                if (!borrow.getBorrowMethod().equalsIgnoreCase(borrowMethod)) {
                    match = false;
                }
            }
    
            String bookStatus = (String) bookReservationStatusComboBox.getSelectedItem();
            if (!bookStatus.isEmpty() && !bookStatus.equals("All")) {
                if (!borrow.getStatus().equalsIgnoreCase(bookStatus)) {
                    match = false;
                }
            }
    
            if (selectedBorrowDate != null && !borrow.getBorrowDate().isEqual(selectedBorrowDate)) {
                match = false;
            }
    
            if (selectedDueDate != null && !borrow.getDueDate().isEqual(selectedDueDate)) {
                match = false;
            }
    
            if (selectedReturnDate != null) {
                if (borrow.getReturnDate() == null || !borrow.getReturnDate().isEqual(selectedReturnDate)) {
                    match = false;
                }
            }
    
            if (match) {
                filteredBorrow.add(borrow);
               
            }
        }
        loadTableData(filteredBorrow);
    }
    

    private void loadTableData(List<BorrowDetail> list) {
        model.setRowCount(0);
        for (BorrowDetail borrow : list) {
            String borrowId = borrow.getId();
            String bookId = borrow.getBook().getBookID();
            String bookName = borrow.getBook().getBookName();
            String borrowBookStatus = borrow.getStatus();
            
            String borrowMethod = borrow.getBorrowMethod();
            String username = borrow.getUser().getName();
            String phoneNumber = borrow.getUser().getPhoneNumber();
            String borrowDate = borrow.getBorrowDate().format(FORMATTER);
            String dueDate = borrow.getDueDate().format(FORMATTER);
            String returnDate = (borrow.getReturnDate() != null) ? borrow.getReturnDate().format(FORMATTER) : "-";

            model.addRow(new Object[]{borrowId,bookId, bookName, borrowDate, username, phoneNumber, dueDate, returnDate, borrowBookStatus,borrowMethod});
        }
    }
}




