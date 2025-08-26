import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;

public class User_HomePage extends JFrame {
    private boolean isMenuVisible = false;
    private final JPanel sidePanel;
    private final UserData currentUser;
    private JFrame profileFrame;
    private boolean isBlackListed;
    private JScrollPane scrollPane;

    public User_HomePage(UserData user) {
        this.currentUser = user;
        this.isBlackListed = checkIfUserIsBlacklisted(user);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        setTitle("User Homepage");
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

        // Menu Button
        JButton menuButton = GUI.createButton("MENU", 10, 10, 80, 40, 14);
        topBar.add(menuButton);

        // Centered Title
        JLabel title = GUI.createLabel("Library", (screenWidth / 2) - 75, 15, 150, 30, 22);
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(title);

        // Right-side Buttons
        JButton notificationBtn = GUI.createNotificationButton(isBlackListed ? "Image/gotNotificationIcon.png" : "Image/notificationIcon.png", screenWidth - 160, 10, 40, 40);

        notificationBtn.addActionListener(e -> {
            List<BlackListDetail> blacklist = notification.loadBlackList("Blacklist.txt");
            new notification(blacklist,currentUser).setVisible(true);
        });

        JButton profileBtn = GUI.createProfileButton(user.getName(),"Image/profile.png",screenWidth - 110,10,100,40);
        
        profileBtn.addActionListener(e -> {
            showProfilePopup(user, "Image/profile.png");
        });
        
        topBar.add(profileBtn);
        topBar.add(notificationBtn);

        backgroundPanel.add(topBar);

        // Side Panel (initially hidden)
        sidePanel = new JPanel(null);
        sidePanel.setBackground(new Color(240, 240, 240));
        sidePanel.setBounds(-200, 60, 200, screenHeight - 60);

        // Sidebar Buttons
        String[] menuItems = {"My Book","Reserve Book","Reserve Room","Pay Fine", "Log Out"};
        int yOffset = 40;
        for (String item : menuItems) {
            JButton btn = GUI.createButton(item, 10, yOffset, 160, 40, 16);
            btn.addActionListener(e -> handleSidebarAction(item));
            
            // Disable buttons if user is blacklisted
            if (isBlackListed && 
                (item.equals("My Book") || item.equals("Reserve Book") || item.equals("Reserve Room"))) {
                btn.setEnabled(false);
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setToolTipText("This feature is disabled because your account is blacklisted");
            }
            
            sidePanel.add(btn);
            yOffset += 60;
        }

       backgroundPanel.add(sidePanel);

        // Toggle side menu
        menuButton.addActionListener(e -> toggleMenu());
        
        setVisible(true);
    }

    private boolean checkIfUserIsBlacklisted(UserData user) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("Blacklist.txt"));
            for (String line : lines) {
                String[] parts = line.split("\t");
                if (parts.length >= 4 && parts[0].equals(user.getUserid()) && parts[3].equals("true")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void showProfilePopup(UserData user, String imagePath) {
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
    
        String[] labels = {"User ID:", "Name:", "Phone Number:", "Status:"};
        String[] values = {
            user.getUserid(), 
            user.getName(), 
            user.getPhoneNumber(),
            isBlackListed ? "Blacklisted" : "Active"
        };
        
        for (int i = 0; i < labels.length; i++) {
            JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            detailPanel.setMaximumSize(new Dimension(280, 40));
            
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            detailPanel.add(label);
            
            JLabel value = new JLabel(values[i]);
            value.setFont(new Font("Arial", Font.PLAIN, 14));
            if (i == 3 && isBlackListed) {
                value.setForeground(Color.RED);
            }
            detailPanel.add(value);
            
            contentPanel.add(detailPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    
        profileFrame.add(contentPanel, BorderLayout.CENTER);
        profileFrame.setVisible(true);
    }

    private void toggleMenu() {
        int targetX = isMenuVisible ? -200 : 0;
        sidePanel.setLocation(targetX, 60);
        isMenuVisible = !isMenuVisible;
        sidePanel.revalidate();
        sidePanel.repaint();
    }

    private void handleSidebarAction(String action) {
        if (isBlackListed && 
            (action.equals("My Book") || action.equals("Reserve Book") || action.equals("Reserve Room"))) {
            JOptionPane.showMessageDialog(this, 
                "Your account has been blacklisted. Please contact the administrator for assistance.", 
                "Access Restricted", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        switch (action) {
            case "My Book":  
                new BookTransactions(currentUser);
                break;
            case "Reserve Book":
                new BookManagementPage(currentUser);
                break;
            
            case "Reserve Room":
                dispose();
                MainReserveRoomGUI.reserveRoom(currentUser);
                break;
            case "Pay Fine":
                List<ProcessFineDetail> processFine = ProcessFineStaff.loadProcessFineFromFile("processFine.txt");
                new ProcessFineCustomer(processFine, currentUser);
                /*for (ProcessFineDetail detail : processFine) {
                    if (detail.getUser() != null) {
                        new ProcessFineCustomer(processFine, currentUser);
                        return; // Only show for one user
                    }
                }*/
                break;
            case "Log Out":
                dispose();
                new User_LoginPage();
                break;
        }
    }
}






class ReserveBookManager {

    
    private UserData currentUser;
    private Book book;
    private String filePath;


        
    public ReserveBookManager(List<Book> bookList,List<BorrowDetail> borrowedList,List<String> bookImageList, UserData user,int index,boolean isFromReturnBook) {
        this.currentUser=user;
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
        
        // Count the number of books already reserved or borrowed (including borrowed with null returnDate)
        int reservedOrBorrowedOrOverdueCount = 0;
        

        // Check both reserved and borrowed books
        for (ReserveBookDetail reservedBook : BookFileManager.getReserveBookList()) {
            if (reservedBook.getUser().equals(currentUser)) {
                // Check if the book is either reserved or borrowed (borrowed and still not returned)
                if (reservedBook.getStatus().equals("Reserved")) {
                    reservedOrBorrowedOrOverdueCount++;
                }
            }
        }

        for (BorrowDetail borrowedBook : BookFileManager.getBorrowedList()) {
            
            if (borrowedBook.getUser().equals(currentUser)) {
                // Check if the book is either reserved or borrowed (borrowed and still not returned)
                if (borrowedBook.getReturnDate() == null&& borrowedBook.getStatus().equals("Overdue")) {
                    reservedOrBorrowedOrOverdueCount++;
                }
            }
        }
        // If user has 4 or more reserved/borrowed books, show error and return
        if (reservedOrBorrowedOrOverdueCount >= 4) {
            JOptionPane.showMessageDialog(
                null,
                "You have reserve and borrow more than 4 book.You cannot reserve and borrow more than 4 books. Please return a book before reserving another.",
                "Reservation Error",
                JOptionPane.ERROR_MESSAGE
            );
            
            return; 
        }



        CalendarGUI calendar = new CalendarGUI(index, borrowedList,bookList,isFromReturnBook);
        calendar.showDialog();

        if (!calendar.getIsConfirmed()) {
            return;
        }

        LocalDate selectedDate = calendar.getSelectedDate();
        String bookReserveId="RB0001";
        for(ReserveBookDetail reserveBook:BookFileManager.getReserveBookList()){
            bookReserveId=reserveBook.generateNextId();
        }
     
        
        if(isFromReturnBook){
            book = borrowedList.get(index - 1).getBook();
            filePath = borrowedList.get(index - 1).getImageFile();

        }else{
            book = bookList.get(index - 1);
            filePath = bookImageList.get(index - 1);

        }
        

        LocalDate bookReservationMadeDate = LocalDate.now();
        LocalDate bookReservationBookDate = selectedDate;
        String bookReservationStatus = "Reserved";

        ReserveBookDetail newReserve = new ReserveBookDetail(
            bookReserveId,
            this.currentUser,
            book,
            bookReservationMadeDate,
            bookReservationBookDate,
            bookReservationStatus,
            filePath
        );

        BookFileManager.getReserveBookList().add(newReserve);
        BookFileManager.updateFile(BookFileManager.getReserveBookFile());
        JOptionPane.showMessageDialog(
            null,
            "Your book reservation was successful.\nReservation made on " + FORMATTER.format(newReserve.getReservationMadeDate()) +
            ".\nReserved Book for " + FORMATTER.format(newReserve.getReservationBookDate()) +
            ".\nPlease collect the book at library at 9:00AM.",
            "Reserve Confirmation",
            JOptionPane.INFORMATION_MESSAGE
        );
    
    }
}



class BookTransactions extends JFrame {

    private static List<String> bookImageList;
    private static List<BorrowDetail> borrowedList;
    private static List<Book> bookList;
    private static List<ReserveBookDetail> reservedList;
    private DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Font font = new Font("SimSun", Font.BOLD, 14);

    private UserData currentUser;
    


    private JPanel actionPanel;
    private JTabbedPane tabbedPane ;
    private JPanel mainPanel;
    public BookTransactions(UserData loggedInUser) {
        this.currentUser=loggedInUser;
        bookList = BookFileManager.getBookList();
        bookImageList=BookFileManager.getBookImageList();
        borrowedList = BookFileManager.getBorrowedList();
        reservedList = BookFileManager.getReserveBookList();

        setTitle("Borrow Book");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

      
        JLabel titleLabel = new JLabel("My Book", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SimSun", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // top padding

 
        add(titleLabel, BorderLayout.NORTH);

        // Main Tabbed Pane
        tabbedPane = new JTabbedPane();

       // Create Reserved Tab normally
        JScrollPane reservedTab = createReservedTab();

        // Create a custom JLabel for the tab title
        JLabel reservedLabel = new JLabel("Reserved");
        reservedLabel.setFont(new Font("SimSun", Font.BOLD, 18)); // <- Bigger Font
        reservedLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane borrowedTab = createBorrowedTab();
        JLabel borrowedLabel = new JLabel("Borrowed");
        borrowedLabel.setFont(new Font("SimSun", Font.BOLD, 18));
        borrowedLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JScrollPane returnTab = createReturnedTab();
        JLabel returnedLabel = new JLabel("Returned");
        returnedLabel.setFont(new Font("SimSun", Font.BOLD, 18));
        returnedLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        tabbedPane.addTab(null, reservedTab);
        tabbedPane.setTabComponentAt(0, reservedLabel);

        tabbedPane.addTab(null, borrowedTab);
        tabbedPane.setTabComponentAt(1, borrowedLabel);

        tabbedPane.addTab(null, returnTab);
        tabbedPane.setTabComponentAt(2, returnedLabel);

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JScrollPane  createReservedTab() {
        JPanel reservedPanel = new JPanel();
        reservedPanel.setLayout(new BoxLayout(reservedPanel, BoxLayout.Y_AXIS));

        // Header
        reservedPanel.add(createHeaderPanel("Reserved"));

        // Title
        JLabel reservedLabel = new JLabel("• Currently Reserved");
        reservedLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        reservedLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        reservedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        reservedPanel.add(reservedLabel);

        
        int displayIndex = 1;



        for (ReserveBookDetail reserve : reservedList) {
            if(!reserve.getUser().equals(this.currentUser)){
                continue;
            }
            String status = reserve.getStatus();
            boolean isreserved = "Reserved".equalsIgnoreCase(status);
           if(isreserved){
                JPanel panel = createReserveBookPanel(displayIndex, reserve.getImageFile(), reserve.getBook(), reserve.getReservationMadeDate(), reserve.getReservationBookDate());
                reservedPanel.add(panel);
                displayIndex++;
            }
            
            
            
        }
      
        

        JScrollPane scrollPane = new JScrollPane(reservedPanel);
        return scrollPane;
    }

    public JPanel createReserveBookPanel(int displayIndex,String imageFilePath, Book book, LocalDate reservationMadeDate, LocalDate reservationBookDate) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
    
        Font font = new Font("SimSun", Font.PLAIN, 14);
    
        // ==== 1. Index Panel ====
        JPanel indexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indexPanel.setPreferredSize(new Dimension(89, 150));
        indexPanel.setMaximumSize(new Dimension(89, 150));
        indexPanel.setMinimumSize(new Dimension(89, 150));
 
        JLabel numberLabel = new JLabel(String.valueOf(displayIndex));
        numberLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        indexPanel.add(numberLabel);
        rowPanel.add(indexPanel);

        
    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 2. Image Panel ====
        JPanel imagePanel = new JPanel();
        
        imagePanel.setMaximumSize(new Dimension(285, 160));
        imagePanel.setMinimumSize(new Dimension(285, 160));
        imagePanel.setPreferredSize(new Dimension(285, 160));
        JLabel imageLabel = new JLabel();

        File file = new File(BookFileManager.getBookImageFile(), imageFilePath);  // Combine folder path and image file name
        

        
        


        
        
        
        try {
            BufferedImage img = ImageIO.read(file);
            
            if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                imageLabel.setIcon(new ImageIcon(img));  // Set the icon with an ImageIcon
            } else {
                imageLabel.setText("Image not available");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Crash in panel creation: " + e.getMessage());
            e.printStackTrace();
        }
        


        imagePanel.add(imageLabel);
        rowPanel.add(imagePanel);
    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 3. Info Panel ====
        JPanel infoPanel = new JPanel();  
        infoPanel.setPreferredSize(new Dimension(300, 150));
        infoPanel.setMaximumSize(new Dimension(300, 150));
        infoPanel.setMinimumSize(new Dimension(300, 150));

       
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Title: " + book.getBookName());
        JLabel authorLabel = new JLabel("Author: " + book.getAuthor());
        titleLabel.setFont(font);
        authorLabel.setFont(font);
        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        rowPanel.add(infoPanel);

        
        
        

    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 4. reservationMadeDate ====
        JPanel reservationMadeDatePanel = new JPanel();
        reservationMadeDatePanel.setPreferredSize(new Dimension(300, 150));
        reservationMadeDatePanel.setMaximumSize(new Dimension(300, 150));
        reservationMadeDatePanel.setMinimumSize(new Dimension(300, 150));

        reservationMadeDatePanel.setLayout(new BoxLayout(reservationMadeDatePanel, BoxLayout.Y_AXIS));
        JLabel reservationMadeDateLabel = new JLabel("Reservation Made Date: " + reservationMadeDate.format(FORMATTER));
        reservationMadeDateLabel.setFont(font);
        
        reservationMadeDatePanel.add(reservationMadeDateLabel);
        
        rowPanel.add(reservationMadeDatePanel);
        
    
        rowPanel.add(createVerticalSeparator());

        // ==== 5. reservationBookDate  ====
        JPanel reservationBookDatePanel = new JPanel();
        reservationBookDatePanel.setPreferredSize(new Dimension(300, 150));
        reservationBookDatePanel.setMaximumSize(new Dimension(300, 150));
        reservationBookDatePanel.setMinimumSize(new Dimension(300, 150));

        reservationBookDatePanel.setLayout(new BoxLayout(reservationBookDatePanel, BoxLayout.Y_AXIS));
        
        JLabel reservationBookDateLabel = new JLabel("Reservation Book Date: " +reservationBookDate.format(FORMATTER));
        reservationBookDateLabel.setFont(font);
        reservationBookDatePanel.add(reservationBookDateLabel);
        rowPanel.add(reservationBookDatePanel);
    
        rowPanel.add(createVerticalSeparator());

        // ==== 6. remarkPanel ====
        JPanel remarkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        remarkPanel.setPreferredSize(new Dimension(220, 150));
        remarkPanel.setMaximumSize(new Dimension(220, 150));
        remarkPanel.setMinimumSize(new Dimension(220, 150));
 
        String remarkText = "Remark: Please collect\n book at library\nat  9:00 am on " + reservationBookDate.format(FORMATTER)+".";
        JLabel remarkLabel = new JLabel("<html>" + remarkText.replace("\n", "<br>") + "</html>"); // Use HTML to handle line breaks

        remarkLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        remarkPanel.add(remarkLabel);
        rowPanel.add(remarkPanel);


        
    
        return rowPanel;
    }


    private JScrollPane createBorrowedTab() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Header
        mainPanel.add(createHeaderPanel("Borrowed"));

        // Sections
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel, BoxLayout.Y_AXIS));
        JLabel currentLabel = new JLabel("•Currently Borrowed");
        currentLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        currentLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        mainPanel.add(currentLabel);
        currentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(currentPanel);

        

        // Fill Data
        int displayIndexBorrow = 1;
        
        for (BorrowDetail borrow : borrowedList) {
            if(!borrow.getUser().equals(this.currentUser)){
                continue;
            }
            boolean isNotReturned = borrow.getReturnDate() == null;
            
            if(isNotReturned){
                JPanel panel = createBorrowBookPanel(displayIndexBorrow, borrow.getImageFile(), borrow.getBook(), borrow.getDueDate(), borrow.getReturnDate(),borrow.getStatus());
        
                currentPanel.add(panel);
            }
            
            
                
         
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        return scrollPane;
    }

    public JPanel createBorrowBookPanel(int displayIndex,String imageFilePath, Book book, LocalDate dueDate, LocalDate returnDate,String status) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
    
        Font font = new Font("SimSun", Font.PLAIN, 14);
    
        // ==== 1. Index Panel ====
        JPanel indexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indexPanel.setPreferredSize(new Dimension(89, 150));
        indexPanel.setMaximumSize(new Dimension(89, 150));
        indexPanel.setMinimumSize(new Dimension(89, 150));
 
        JLabel numberLabel = new JLabel(String.valueOf(displayIndex));
        numberLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        indexPanel.add(numberLabel);
        rowPanel.add(indexPanel);

        
    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 2. Image Panel ====
        JPanel imagePanel = new JPanel();
        
        imagePanel.setMaximumSize(new Dimension(285, 160));
        imagePanel.setMinimumSize(new Dimension(285, 160));
        imagePanel.setPreferredSize(new Dimension(285, 160));
        JLabel imageLabel = new JLabel();

        File file = new File(BookFileManager.getBookImageFile(), imageFilePath);  // Combine folder path and image file name
        

        
        
        
        try {
            BufferedImage img = ImageIO.read(file);
            
            if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                imageLabel.setIcon(new ImageIcon(img));  // Set the icon with an ImageIcon
            } else {
                imageLabel.setText("Image not available");
            }
        } catch (Exception e) {
           JOptionPane.showMessageDialog(null, "Crash in panel creation: " + e.getMessage());
            e.printStackTrace();
        }
        


        imagePanel.add(imageLabel);
        rowPanel.add(imagePanel);
    
        rowPanel.add(createVerticalSeparator());
     
        // ==== 3. Info Panel ====
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(500, 150));
        infoPanel.setMaximumSize(new Dimension(500, 150));
        infoPanel.setMinimumSize(new Dimension(500, 150));

       
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Title: " + book.getBookName());
        JLabel authorLabel = new JLabel("Author: " + book.getAuthor());
        titleLabel.setFont(font);
        authorLabel.setFont(font);
        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        rowPanel.add(infoPanel);

        
        
        

    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 4. Dates Panel ====
        JPanel dueDatePanel = new JPanel();
        dueDatePanel.setPreferredSize(new Dimension(179, 150));
        dueDatePanel.setMaximumSize(new Dimension(179, 150));
        dueDatePanel.setMinimumSize(new Dimension(179, 150));

        dueDatePanel.setLayout(new BoxLayout(dueDatePanel, BoxLayout.Y_AXIS));
        JLabel dueLabel = new JLabel("Due: " + dueDate.format(FORMATTER));
        dueLabel.setFont(font);
        
        dueDatePanel.add(dueLabel);
        
        rowPanel.add(dueDatePanel);

    
        rowPanel.add(createVerticalSeparator());

        // ==== 5. Return Date ====
        JPanel returnDatePanel = new JPanel();
        returnDatePanel.setPreferredSize(new Dimension(179, 150));
        returnDatePanel.setMaximumSize(new Dimension(179, 150));
        returnDatePanel.setMinimumSize(new Dimension(179, 150));

        returnDatePanel.setLayout(new BoxLayout(returnDatePanel, BoxLayout.Y_AXIS));
        
        JLabel returnedLabel = new JLabel("Returned: " + (returnDate != null ? returnDate.format(FORMATTER) : "-"));
        returnedLabel.setFont(font);
        returnDatePanel.add(returnedLabel);
        rowPanel.add(returnDatePanel);
    
        rowPanel.add(createVerticalSeparator());

        
        JPanel remarkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        remarkPanel.setPreferredSize(new Dimension(270, 150));
        remarkPanel.setMaximumSize(new Dimension(270, 150));
        remarkPanel.setMinimumSize(new Dimension(270, 150));
        
        remarkPanel.setLayout(new BoxLayout(remarkPanel, BoxLayout.Y_AXIS)); // Vertical layout
     
        
        
        JLabel remarkLabel;
        if (status.trim().equalsIgnoreCase("Overdue")) {
            remarkLabel = new JLabel("<html><span style='color:red;'>Book is overdue.<br>Please return the book quickly.</span></html>");
        } else {
            remarkLabel = new JLabel("<html>Please return the book on<br>" + dueDate.format(FORMATTER) + ".</html>");
        }
        remarkLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        remarkPanel.add(remarkLabel);
        rowPanel.add(remarkPanel);

        
        
    
        return rowPanel;
    }


    private JScrollPane  createReturnedTab() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Header
        mainPanel.add(createHeaderPanel("Returned"));

        

        JPanel returnedPanel = new JPanel();
        returnedPanel.setLayout(new BoxLayout(returnedPanel, BoxLayout.Y_AXIS));
        JLabel returnedLabel = new JLabel("• Already Returned");
        returnedLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        returnedLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));
        mainPanel.add(returnedLabel);
        returnedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(returnedPanel);

        // Fill Data
        int displayIndexReturn = 1;
        
        for (BorrowDetail borrow : borrowedList) {
            
            if(!borrow.getUser().equals(this.currentUser)){
                continue;
            }
            boolean isReturned = borrow.getReturnDate() != null;

            if (isReturned) {
                 
                JPanel panel = createReturnBookPanel(displayIndexReturn, borrow.getImageFile(), borrow.getBook(), borrow.getDueDate(), borrow.getReturnDate());
            
                panel.setBackground(new Color(240, 240, 240)); // light gray for returned
                returnedPanel.add(panel);
                displayIndexReturn++;
                

            } 
 
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        return scrollPane;
    }



    public JPanel createReturnBookPanel(int displayIndex,String imageFilePath, Book book, LocalDate dueDate, LocalDate returnDate) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
    
        Font font = new Font("SimSun", Font.PLAIN, 14);
    
        // ==== 1. Index Panel ====
        JPanel indexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indexPanel.setPreferredSize(new Dimension(89, 150));
        indexPanel.setMaximumSize(new Dimension(89, 150));
        indexPanel.setMinimumSize(new Dimension(89, 150));
 
        JLabel numberLabel = new JLabel(String.valueOf(displayIndex));
        numberLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        indexPanel.add(numberLabel);
        rowPanel.add(indexPanel);

        
    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 2. Image Panel ====
        JPanel imagePanel = new JPanel();
        
        imagePanel.setMaximumSize(new Dimension(285, 160));
        imagePanel.setMinimumSize(new Dimension(285, 160));
        imagePanel.setPreferredSize(new Dimension(285, 160));
        JLabel imageLabel = new JLabel();

        File file = new File(BookFileManager.getBookImageFile(), imageFilePath);  // Combine folder path and image file name
        

        
       
        
        
        try {
            BufferedImage img = ImageIO.read(file);
            
            if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
                imageLabel.setIcon(new ImageIcon(img));  // Set the icon with an ImageIcon
            } else {
                imageLabel.setText("Image not available");
            }
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, "Crash in panel creation: " + e.getMessage());
            e.printStackTrace();
        }
        


        imagePanel.add(imageLabel);
        rowPanel.add(imagePanel);
    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 3. Info Panel ====
        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(600, 150));
        infoPanel.setMaximumSize(new Dimension(600, 150));
        infoPanel.setMinimumSize(new Dimension(600, 150));

       
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Title: " + book.getBookName());
        JLabel authorLabel = new JLabel("Author: " + book.getAuthor());
        titleLabel.setFont(font);
        authorLabel.setFont(font);
        infoPanel.add(titleLabel);
        infoPanel.add(authorLabel);
        rowPanel.add(infoPanel);

        
        
        

    
        rowPanel.add(createVerticalSeparator());
    
        // ==== 4. Dates Panel ====
        JPanel dueDatePanel = new JPanel();
        dueDatePanel.setPreferredSize(new Dimension(179, 150));
        dueDatePanel.setMaximumSize(new Dimension(179, 150));
        dueDatePanel.setMinimumSize(new Dimension(179, 150));

        dueDatePanel.setLayout(new BoxLayout(dueDatePanel, BoxLayout.Y_AXIS));
        JLabel dueLabel = new JLabel("Due: " + dueDate.format(FORMATTER));
        dueLabel.setFont(font);
        
        dueDatePanel.add(dueLabel);
        
        rowPanel.add(dueDatePanel);

    
        rowPanel.add(createVerticalSeparator());

        // ==== 5. Return Date ====
        JPanel returnDatePanel = new JPanel();
        returnDatePanel.setPreferredSize(new Dimension(179, 150));
        returnDatePanel.setMaximumSize(new Dimension(179, 150));
        returnDatePanel.setMinimumSize(new Dimension(179, 150));

        returnDatePanel.setLayout(new BoxLayout(returnDatePanel, BoxLayout.Y_AXIS));
        
        JLabel returnedLabel = new JLabel("Returned: " + (returnDate != null ? returnDate.format(FORMATTER) : "-"));
        returnedLabel.setFont(font);
        returnDatePanel.add(returnedLabel);
        rowPanel.add(returnDatePanel);
    
        rowPanel.add(createVerticalSeparator());

        
        // ==== 6. Reserve Button ====
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS)); // Vertical layout

        actionPanel.setMaximumSize(new Dimension(100, 150));
        actionPanel.setMinimumSize(new Dimension(100, 150));
        actionPanel.setPreferredSize(new Dimension(100, 150));

        

        

        JButton reserveButton = new JButton("Reserve");
        reserveButton.setFont(font);
        reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally

    
        actionPanel.add(Box.createVerticalStrut(10)); // optional spacing
        actionPanel.add(reserveButton);
        
        
      
        for (int i = 0; i < borrowedList.size(); i++) {
            
        
            BorrowDetail borrow = borrowedList.get(i);
            if (borrow.getBook().equals(book)&&(borrow.getStatus().equals("Returned")||borrow.getStatus().equals("Overdue"))) {
                
                
                final int CURRENT_INDEX = i+1;
                reserveButton.addActionListener(e -> {
                    boolean isFromReturnBook=true;
                    new ReserveBookManager(bookList,borrowedList,bookImageList,this.currentUser,CURRENT_INDEX,isFromReturnBook);
                    JScrollPane updatedReservedTab = createReservedTab();
                    tabbedPane.setComponentAt(0, updatedReservedTab);
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                });
                break;
                
            }
        }
            
       
        

            
        
        rowPanel.add(actionPanel);
    
        return rowPanel;
    }

    


    private JPanel createHeaderPanel(String tabString) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        headerPanel.setPreferredSize(new Dimension(headerPanel.getPreferredSize().width, 20));
        headerPanel.setMinimumSize(new Dimension(headerPanel.getMinimumSize().width, 20));

        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        headerPanel.add(createHeaderLabel("No.", 90));
        headerPanel.add(createVerticalSeparator());
        headerPanel.add(createHeaderLabel("Image", 285));
        headerPanel.add(createVerticalSeparator());
        if(tabString.equals("Reserved")){
            headerPanel.add(createHeaderLabel("Book Info", 300));
        }else if(tabString.equals("Borrowed")){
            headerPanel.add(createHeaderLabel("Book Info", 500));
        }else{
            headerPanel.add(createHeaderLabel("Book Info", 600));
        
        }
         
        headerPanel.add(createVerticalSeparator());
        if (tabString.equals("Returned")){
            
            headerPanel.add(createHeaderLabel("Due Date", 179));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Return Date", 179));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Action", 100));

            
        }else if (tabString.equals("Reserved")){
            headerPanel.add(createHeaderLabel("Reservation Made Date", 300));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Reservation Book Date", 300));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Remark", 220));

        }else{
            headerPanel.add(createHeaderLabel("Due Date", 179));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Return Date", 179));
            headerPanel.add(createVerticalSeparator());
            headerPanel.add(createHeaderLabel("Remark", 270));

        }
        
        return headerPanel;
    }

    private JPanel createHeaderLabel(String text, int width) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setPreferredSize(new Dimension(width, 20));
        wrapper.setMaximumSize(new Dimension(width, 20));
        wrapper.setMinimumSize(new Dimension(width, 20));
    
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        wrapper.add(label, BorderLayout.CENTER);
    
        return wrapper;
    }

    private JSeparator createVerticalSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 140));
        JPanel sepPanel = new JPanel();
        sepPanel.setPreferredSize(new Dimension(1, 150));
        sepPanel.setLayout(new BorderLayout());
        sepPanel.add(separator, BorderLayout.CENTER);
        return separator;
    }

    
    
    
}


class CalendarGUI extends JFrame {
    private JLabel monthLabel;
    private JComboBox<Integer> yearComboBox;
    private JPanel calendarPanel;
    private Calendar calendar;
    
   
    
    private LocalDate selectedDate;
    private boolean isConfirmed;
    private static JDialog calendarDialog = new JDialog();
    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public  boolean getIsConfirmed() {
        return isConfirmed;
    }
    private int indexOfBook;
    private List<Book> calendarBookList;
    private List<BorrowDetail> calendarBorrowedList;
    private boolean isSameReserveBook;
    private boolean isSameBorrowBook;
    private boolean isFromReturnBooks;
    public CalendarGUI(int index,List<BorrowDetail> borrowedList,List<Book> bookList,boolean isFromReturnBook) {
        isFromReturnBooks=isFromReturnBook;
        isConfirmed=false;
        calendarBookList=bookList;
        calendarBorrowedList=borrowedList;
        indexOfBook=index;
        calendarDialog = new JDialog(this, "Select Date", true);
        calendarDialog.setSize(500, 500);
        calendarDialog.setLocationRelativeTo(this); // Center the dialog

        // Use a panel to hold everything inside the dialog
        JPanel contentPanel = new JPanel(new BorderLayout());

        calendar = Calendar.getInstance();  //default date and time in system
        monthLabel = new JLabel("", JLabel.CENTER);

        JButton calenderPrevButton = new JButton("<");
        JButton calenderNextButton = new JButton(">");

        calenderPrevButton.addActionListener(e -> changeMonth(-1));
        calenderNextButton.addActionListener(e -> changeMonth(1));

        JPanel calenderTopPanel = new JPanel(new BorderLayout());
        JPanel calenderNavPanel = new JPanel();
        calenderNavPanel.add(calenderPrevButton);
        calenderNavPanel.add(monthLabel);
        calenderNavPanel.add(calenderNextButton);

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

        calenderTopPanel.add(calenderNavPanel, BorderLayout.CENTER);
        contentPanel.add(calenderTopPanel, BorderLayout.NORTH);
        contentPanel.add(calendarPanel, BorderLayout.CENTER);
        contentPanel.add(yearPanel, BorderLayout.SOUTH);

        calendarDialog.setContentPane(contentPanel);
        updateCalendar();
    }

    public void showDialog() {
        calendarDialog.setVisible(true);
    } 
    
    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta); 

        yearComboBox.setSelectedItem(calendar.get(Calendar.YEAR));

        updateCalendar();
        
    }

    private void updateCalendar() {
        
        calendar.set(Calendar.YEAR, (int) yearComboBox.getSelectedItem());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int month = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String[] headers = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        calendarPanel.removeAll();
        for (String header : headers) {
            calendarPanel.add(new JLabel(header, JLabel.CENTER));
        }

        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel("")); 
        }
        

        for (int day = 1; day <= daysInMonth; day++) {
            final int DAYS=day;
            final JLabel DAY_LABEL = new JLabel(String.valueOf(day), JLabel.CENTER);
            

            calendarPanel.add(DAY_LABEL);
            

            

            String monthsString=String.valueOf(month+1);
            String daysString=String.valueOf(DAYS);
            if(monthsString.length()==1){
                monthsString="0"+monthsString;
            }
            if(daysString.length()==1){
                daysString="0"+daysString;
            }
            String dateString = daysString+"/"+monthsString+"/"+yearComboBox.getSelectedItem().toString();  // Example string
            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy",Locale.ENGLISH);

            LocalDate clickedDate = LocalDate.parse(dateString, FORMATTER);

            
            boolean isPast = clickedDate.isBefore(LocalDate.now()) || clickedDate.equals(LocalDate.now());
            
            
            boolean isReserved = false;
            boolean isReservedByOthers=false;
            for (ReserveBookDetail reserveBook : BookFileManager.getReserveBookList()) {
                LocalDate reservedDate = reserveBook.getReservationBookDate();
                if(isFromReturnBooks){
                    isSameReserveBook=calendarBorrowedList.get(indexOfBook - 1).getBook().equals(reserveBook.getBook());
                }else{
                    isSameReserveBook = calendarBookList.get(indexOfBook - 1).equals(reserveBook.getBook());

                }
                
                // Check if the clicked date is within 14 days of the reserved date,then will break the loop,go to red word
                boolean isWithin7DaysAfterReserve = !clickedDate.isBefore(reservedDate) && !clickedDate.isAfter(reservedDate.plusDays(6));
                boolean isWithin7DaysBeforeReserve = !clickedDate.isAfter(reservedDate) && !clickedDate.isBefore(reservedDate.minusDays(6));

                // Check for reserved date exact match or within 14 days
                if (isSameReserveBook && clickedDate.equals(reservedDate)) {
                    isReserved = true;   
                    break;
                }

                if (isSameReserveBook && isWithin7DaysAfterReserve) {
                    isReservedByOthers = true;  
                    break;
                }
                
                
                if (isSameReserveBook && isWithin7DaysBeforeReserve) {
                    isReservedByOthers = true; 
                    break; 
                }
                
                
            }

            if (isReserved||isReservedByOthers) {
                DAY_LABEL.setForeground(Color.RED);
            }
            else if (isPast) {
                DAY_LABEL.setForeground(Color.GRAY);
            } else {
                DAY_LABEL.setForeground(Color.BLUE);
            }

          
            boolean isBorrowedByOther=false;
            boolean isAlreadyBorrowedByOther=false;
            for (BorrowDetail borrowBook : BookFileManager.getBorrowedList()) {
                LocalDate borrowDate = borrowBook.getBorrowDate();
                if(isFromReturnBooks){
                    isSameBorrowBook=calendarBorrowedList.get(indexOfBook - 1).getBook().equals(borrowBook.getBook());
                }else{
                    isSameBorrowBook = calendarBookList.get(indexOfBook - 1).equals(borrowBook.getBook());

                }
                
                if (!isSameBorrowBook) continue;

                LocalDate today = LocalDate.now();
                LocalDate sevenDaysAgo = today.minusDays(6); // last 7 days includes today

                boolean borrowedWithinLast7Days = !borrowDate.isBefore(sevenDaysAgo) && !borrowDate.isAfter(today);

                if (borrowedWithinLast7Days) {
                    LocalDate blockStart = today;
                    LocalDate blockEnd = borrowDate.plusDays(6);
                
                    if (!clickedDate.isBefore(blockStart) && !clickedDate.isAfter(blockEnd)) {
                        isAlreadyBorrowedByOther = true;
                        
                    }
                }
                
                if (isSameBorrowBook && isAlreadyBorrowedByOther) {
                    isBorrowedByOther = true;  
                    break;
                    
                }

            }
          
            if (isBorrowedByOther) {
                DAY_LABEL.setForeground(Color.RED);
            }
            
            DAY_LABEL.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

            DAY_LABEL.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selectedDate = clickedDate;
                    boolean isReserved = DAY_LABEL.getForeground().equals(Color.RED);
                    boolean isReservedByOthers= DAY_LABEL.getForeground().equals(Color.RED);
                    boolean isPast = DAY_LABEL.getForeground().equals(Color.GRAY);
                    String message;
            
                    if (isReserved||isReservedByOthers) {
                        message = selectedDate.format(FORMATTER) + " is already reserved.";
                    } else if(isPast){
                        message = selectedDate.format(FORMATTER) + " has already passed.";
                    
                    }else{
                        message = "You selected: " + selectedDate.format(FORMATTER) + 
                        "\nWould you like to reserve the book for this date?";

                    }
            
                    // Custom dialog panel
                    JPanel panel = new JPanel(new BorderLayout(10, 10));
                    JLabel label = new JLabel("<html><p style='width:200px'>" + message + "</p></html>");
                    panel.add(label, BorderLayout.CENTER);
            
                    // Buttons
                    JButton confirmButton = new JButton("Ok");
                    JButton cancelButton = new JButton("Cancel");
            
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(cancelButton);
                    buttonPanel.add(confirmButton);
                    panel.add(buttonPanel, BorderLayout.SOUTH);
            
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Date Details");
                    dialog.setModal(true);
                    dialog.getContentPane().add(panel);
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
            
                    confirmButton.addActionListener(evt -> {
                        if(isPast){
                            dialog.dispose();
                            
                        }else if (!isReserved) {
                            isConfirmed=true;
                            JOptionPane.showMessageDialog(dialog, "Date confirmed: " + selectedDate.format(FORMATTER));
                            dialog.dispose();
                            calendarDialog.dispose();
                            updateCalendar();
                        }
                        
                    });
            
                    cancelButton.addActionListener(evt -> dialog.dispose());
            
                    dialog.setVisible(true);
                }
            });
            
        }

        

        calendarPanel.revalidate();
        calendarPanel.repaint();

        monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, java.util.Locale.getDefault()));

        
    }

}
