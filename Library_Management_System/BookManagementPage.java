import java.awt.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.*;



import java.awt.image.BufferedImage;

import java.util.List;
import java.awt.event.*;




public class BookManagementPage extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private JButton resetButton;
    private JLabel advancedFunctionLabel;
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel bottomPanel;
    private JButton nextButton, prevButton;
    
    private List<Book> bookList;
    private List<String> bookImageList;
    private int currentPage = 0;
    private static final int BOOKS_PER_PAGE = 5;
    private JPanel filterPanel;
    private JPanel navigatePanel;
   
    
    
    private JPanel paginationPanel;
    private JLabel showNumberOfBookLabel;
    private JPanel topPanel;
    private static List<BorrowDetail> borrowedList;


    private List<Book> normalSearchResult = new ArrayList<>();
    private List<Book> advancedFilterResult = new ArrayList<>();
    private List<String> normalImageResult = new ArrayList<>();
    private List<String> advancedImageResult = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();
    private List<String> filterBookImageList = new ArrayList<>();

    private JComboBox<String> languageBox, genreBox, availabilityBox;
    private JTextField minYearField, maxYearField, minPagesField, maxPagesField;
    private JButton filterButton,resetAdvancedButton;
    private Font fieldFont = new Font("SimSun", Font.PLAIN, 16);
    private JComponent[] filterComponents;
    private StringBuilder filterInfo = new StringBuilder();
    

    private JPanel middlePanel;

    private JPanel centerWrapperPanel;

    private JDialog advancedFilterDialog;

    private UserData currentUser;

    private StaffData currentStaff;

    private Data people;

public BookManagementPage(Data person) {
    
    people=person;
    if (people instanceof UserData) {
        this.currentUser=(UserData)people;
    } else if (people instanceof StaffData) {
        this.currentStaff=(StaffData)people;
        
    }
    
    setTitle("Book Management System");
    setSize(700, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    setLayout(new BorderLayout());
    setExtendedState(JFrame.MAXIMIZED_BOTH);


    bookList = BookFileManager.getBookList();  
    bookImageList = BookFileManager.getBookImageList();
    borrowedList = BookFileManager.getBorrowedList();
    
    // Create a panel with a proper layout
    topPanel = new JPanel(new BorderLayout());

    // Search panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    searchField = new JTextField(20);
    searchButton = new JButton("Search");
    resetButton = new JButton("Reset");  

    searchPanel.add(new JLabel("Enter search:"));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(resetButton);
  
  
    
    advancedFunctionLabel = new JLabel("Advanced Function");
    advancedFunctionLabel.setForeground(Color.BLUE);
    advancedFunctionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    // Prevent movement issues by setting a fixed font size
    advancedFunctionLabel.setFont(new Font("SimSun", Font.PLAIN, 14));

    // Mouse click event to open the advanced filter page
    advancedFunctionLabel.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            AdvancedFilter();
        }
    });
    

    // Wrap in a panel to fix its position
    JPanel advancedPanel = new JPanel();
    advancedPanel.add(advancedFunctionLabel);

    
    // Add both panels to the topPanel
    topPanel.add(searchPanel, BorderLayout.NORTH);

    
    
    topPanel.add(advancedPanel, BorderLayout.CENTER);

    // Add to frame
    add(topPanel, BorderLayout.NORTH);

    //  Create book list panel (CENTER)
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    scrollPane = new JScrollPane(mainPanel);

                    
    centerWrapperPanel = new JPanel();
    centerWrapperPanel.setLayout(new BoxLayout(centerWrapperPanel, BoxLayout.Y_AXIS));


    middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    middlePanel.setPreferredSize(new Dimension(700, 50));
    middlePanel.add(new JLabel());

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
    headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));
    headerPanel.setPreferredSize(new Dimension(700, 30));
    
    headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
    

    headerPanel.add(createHeaderLabel("No.", 40));
    headerPanel.add(createVerticalSeparator());
    headerPanel.add(createHeaderLabel("Image", 270));
    headerPanel.add(createVerticalSeparator());
    headerPanel.add(createHeaderLabel("Book Info", 500));
    headerPanel.add(createVerticalSeparator());
    headerPanel.add(createHeaderLabel("Author", 420));
    headerPanel.add(createVerticalSeparator());
    if (people instanceof UserData) {
        headerPanel.add(createHeaderLabel("Action", 150));

    } else if (people instanceof StaffData) {
        headerPanel.add(createHeaderLabel("Availability", 200));
    }

    
    
   // Create a scrollable area for the book list (without the header)
    scrollPane = new JScrollPane(mainPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Optional: smoother scrolling

    // Create a wrapper panel for the header and scrollable content
    JPanel scrollContentPanel = new JPanel();
    scrollContentPanel.setLayout(new BoxLayout(scrollContentPanel, BoxLayout.Y_AXIS));

    // Add header panel at the top (fixed, non-scrollable)
    scrollContentPanel.add(headerPanel);

    scrollContentPanel.add(scrollPane);
    

    // === Other sections ===
    centerWrapperPanel = new JPanel();
    centerWrapperPanel.setLayout(new BoxLayout(centerWrapperPanel, BoxLayout.Y_AXIS));

    middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    middlePanel.setPreferredSize(new Dimension(700, 50));
    middlePanel.add(new JLabel()); 

    middlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    

    
    centerWrapperPanel.add(middlePanel);
    centerWrapperPanel.add(scrollContentPanel); 

    add(centerWrapperPanel, BorderLayout.CENTER);
    


    bottomPanel = new JPanel();
    prevButton = new JButton("Previous");
    nextButton = new JButton("Next");

    prevButton.addActionListener(e -> changePage(-1));
    nextButton.addActionListener(e -> changePage(1));

    bottomPanel.add(prevButton);


    paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bottomPanel.add(paginationPanel); 

    bottomPanel.add(nextButton);


    add(bottomPanel, BorderLayout.SOUTH);

    resetButton.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Do you really want to reset?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION
        );
    
        if (confirm == JOptionPane.YES_OPTION) {
            resetFilters();
        }
    });

    searchButton.addActionListener((ActionEvent e) -> {
        middlePanel.remove(showNumberOfBookLabel);
        String query = searchField.getText().toLowerCase();
        normalSearch(query);
    
        
    });

    searchField.addActionListener((ActionEvent e) -> {
        searchButton.doClick(); // Pressing Enter triggers search
    });

    updatePage(); // Load initial book list
    
    setVisible(true);

    
    
}



public void resetFilters(){
    
    middlePanel.remove(showNumberOfBookLabel);
    filterInfo.setLength(0);
    normalSearchResult.clear();  // Clear normal search results
    advancedFilterResult.clear();  // Clear advanced filter results
    bookList = BookFileManager.getBookList();
    bookImageList = BookFileManager.getBookImageList();
    currentPage=0;
    updatePage();
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

private void addRow(JPanel panel,GridBagConstraints gbc, int row, String label, JComponent component) {
    JLabel jLabel = new JLabel(label);
    jLabel.setFont(fieldFont);
    component.setFont(fieldFont);
    if (component instanceof JTextField) {
        ((JTextField) component).setColumns(15);
    }
    gbc.gridx = 0;
    gbc.gridy = row;
    panel.add(jLabel, gbc);         
    gbc.gridx = 1;
    panel.add(component, gbc);      
}


private JTextField createTextField() {
    JTextField textField = new JTextField(15);
    textField.setFont(fieldFont);
    return textField;
}

private String[] getUniqueValues(String field) {
    Set<String> uniqueValues = new TreeSet<>();
    uniqueValues.add(""); // Empty option
    

    for (Book book : BookFileManager.getBookList()) {
        switch (field.toLowerCase()) {
            case "language":
                uniqueValues.add(book.getLanguage());
                break;
            case "genre":
                uniqueValues.add(book.getGenre());
                break;
            case "availability":
                if (people instanceof StaffData) {
                    uniqueValues.add(book.isAvailable()?"Available":"Inavailable");
                    uniqueValues.add(book.isAvailable()?"Inavailable":"Available");
                }
                break;
        }
    }
    return uniqueValues.toArray(new String[0]);
}

// Update books based on search query
private void normalSearch(String query) {
    int count=0;
    

    normalSearchResult.clear();
    normalImageResult.clear();
    
    for (Book book : BookFileManager.getBookList()) {
        String bookText = book.toWord();
        
        
        if (bookText.toLowerCase().contains(query.toLowerCase())) {
            
            normalSearchResult.add(book);
            normalImageResult.add(BookFileManager.getBookImageList().get(count));
            
        }
        count++;
    }

    updateFilteredResultsIfNeeded();
    
}



private void AdvancedFilter() {
    advancedFilterDialog = new JDialog(this, "Advanced Filters", true);
    advancedFilterDialog.setSize(550, 500);
    advancedFilterDialog.setLocationRelativeTo(this); // center on the parent window


    JPanel advancedFilterPanel = new JPanel(new BorderLayout());

    filterPanel = new JPanel(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    Dimension fieldSize = new Dimension(400, 30); // Width x Height

    languageBox = new JComboBox<>(getUniqueValues("language"));
    genreBox = new JComboBox<>(getUniqueValues("genre"));
    if (people instanceof StaffData) {
        availabilityBox = new JComboBox<>(getUniqueValues("availability"));
        availabilityBox.setPreferredSize(fieldSize);
        
    }
    
    minYearField = createTextField();
    maxYearField = createTextField();
    minPagesField = createTextField();
    maxPagesField = createTextField();

    
    languageBox.setPreferredSize(fieldSize);
    genreBox.setPreferredSize(fieldSize);
    

    minYearField.setPreferredSize(fieldSize);
    maxYearField.setPreferredSize(fieldSize);
    minPagesField.setPreferredSize(fieldSize);
    maxPagesField.setPreferredSize(fieldSize);

    if (people instanceof StaffData) {
        filterComponents = new JComponent[] {
            languageBox,
            genreBox,
            minYearField,
            maxYearField,
            minPagesField,
            maxPagesField,
            
            availabilityBox
        };

    }else{
        filterComponents = new JComponent[] {
            languageBox,
            genreBox,
            minYearField,
            maxYearField,
            minPagesField,
            maxPagesField,
        };
    
    }

    for (int i = 0; i < filterComponents.length; i++) {
        int index = i;
        filterComponents[i].addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN && index < filterComponents.length - 1) {
                    filterComponents[index + 1].requestFocus();
                } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP && index > 0) {
                    filterComponents[index - 1].requestFocus();
                }else if(e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
                    applyFilter();
                }
            }
        });
    }

    filterButton = new JButton("Apply Filter");
    resetAdvancedButton = new JButton("Reset");

    filterButton.setFont(fieldFont);
    resetAdvancedButton.setFont(fieldFont);

    addRow(filterPanel, gbc, 0, "Language:", languageBox);
    addRow(filterPanel, gbc, 1, "Genre:", genreBox);
    addRow(filterPanel, gbc, 2, "Min Year:", minYearField);
    addRow(filterPanel, gbc, 3, "Max Year:", maxYearField);
    addRow(filterPanel, gbc, 4, "Min Pages:", minPagesField);
    addRow(filterPanel, gbc, 5, "Max Pages:", maxPagesField);
    if (people instanceof StaffData) {
        addRow(filterPanel, gbc, 6, "Availability:", availabilityBox);
    

    }

    navigatePanel = new JPanel(new GridBagLayout());
    // Buttons
    gbc.gridx = 0;
    gbc.gridy = 6;

    navigatePanel.add(filterButton, gbc);
    
    gbc.gridx = 1;
    
    navigatePanel.add(resetAdvancedButton, gbc); // ✅ Use the dialog's own reset button

    // Button actions
    filterButton.addActionListener(e -> applyFilter());
    resetAdvancedButton.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Do you really want to reset?",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION
        );
    
        if (confirm == JOptionPane.YES_OPTION) {
            resetAdvancedFilters();
        }
    });
    advancedFilterPanel.add(filterPanel, BorderLayout.CENTER);
    advancedFilterPanel.add(navigatePanel, BorderLayout.SOUTH);
    

    advancedFilterDialog.add(advancedFilterPanel);
    advancedFilterDialog.setVisible(true);
    
   
}




private void applyFilter() {
    advancedFilterResult.clear();
    advancedImageResult.clear();

    
    String selectedLanguage = (String) languageBox.getSelectedItem();
    String selectedGenre = (String) genreBox.getSelectedItem();
    
    
    boolean filterByLanguage = selectedLanguage != null && !selectedLanguage.isEmpty();
    boolean filterByGenre = selectedGenre != null && !selectedGenre.isEmpty();
    
    boolean filterByAvailability=false;
    String selectedAvailability=null;
    if (people instanceof StaffData) {
        selectedAvailability = (String) availabilityBox.getSelectedItem();

        filterByAvailability = selectedAvailability != null && !selectedAvailability.isEmpty();

        
    }
    
    int minYear=-1;
    int maxYear=-1;
    int minPages=-1;
    int maxPages=-1;

    try {
        if (!minYearField.getText().isEmpty())
            minYear = Integer.parseInt(minYearField.getText());
        if (!maxYearField.getText().isEmpty())
            maxYear = Integer.parseInt(maxYearField.getText());

        if (!minPagesField.getText().isEmpty())
            minPages = Integer.parseInt(minPagesField.getText());
        if (!maxPagesField.getText().isEmpty())
            maxPages = Integer.parseInt(maxPagesField.getText());

        if (minYear > maxYear) {
            JOptionPane.showMessageDialog(this, "Minimum year cannot be greater than maximum year.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (minPages > maxPages) {
            JOptionPane.showMessageDialog(this, "Minimum pages cannot be greater than maximum pages.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Please enter valid numbers for year and pages.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    boolean filterByYear = !minYearField.getText().isEmpty() && !maxYearField.getText().isEmpty();
    
    

    boolean filterByPages = !minPagesField.getText().isEmpty() && !maxPagesField.getText().isEmpty();
    


    int i=0;
    bookImageList=BookFileManager.getBookImageList();
    
    for (Book book : BookFileManager.getBookList()) {

        boolean matches = true;
        
        if (filterByLanguage && !book.getLanguage().equalsIgnoreCase(selectedLanguage)) {
            matches = false;
        }

        
        if (filterByGenre && !book.getGenre().equalsIgnoreCase(selectedGenre)) {
            matches = false;
        }

        if (filterByYear) {
            if (book.getPublicationYear() < minYear || book.getPublicationYear() > maxYear) {
                matches = false;
            }
        }
        
        if (filterByPages) {
            if (book.getNumberofPage() < minPages || book.getNumberofPage() > maxPages) {
                matches = false;
            }
        }
        
        if (people instanceof StaffData) {
            if (filterByAvailability && !(book.isAvailable() ? "available" : "inavailable").equalsIgnoreCase(selectedAvailability)) {
            matches = false;
            }
            
        }
        
        

        if (matches) {
             
            advancedFilterResult.add(book);
            advancedImageResult.add(BookFileManager.getBookImageList().get(i));
            
        }

        filterInfo = new StringBuilder();

            if (filterByLanguage) {
                filterInfo.append(" | Language: ").append(selectedLanguage);
            }
            if (filterByGenre) {
                filterInfo.append(" | Genre: ").append(selectedGenre);
            }
            if (people instanceof StaffData) {
                if (filterByAvailability) {
                filterInfo.append(" | Availability: ").append(selectedAvailability);
                }
                
            }
            if (filterByYear) {
                filterInfo.append(" | Year: ").append(minYearField.getText()).append(" - ").append(maxYearField.getText());
            }
            if (filterByPages) {
                filterInfo.append(" | Pages: ").append(minPagesField.getText()).append(" - ").append(maxPagesField.getText());
            }
        i++;
    }

   


    updateFilteredResultsIfNeeded();
    
}

private void updateFilteredResultsIfNeeded() {
    middlePanel.remove(showNumberOfBookLabel);
    currentPage=0;
    if (!normalSearchResult.isEmpty() && !advancedFilterResult.isEmpty()) {
        updateCommonResults();
    } else if (!normalSearchResult.isEmpty()) {
        bookList = normalSearchResult;
        bookImageList = normalImageResult;
        int response = JOptionPane.showOptionDialog(
            this, 
            "Filtered " + bookList.size() + " books.", 
            "Information", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            new Object[] {"OK"}, 
            "OK"
        );
        if (response == JOptionPane.OK_OPTION) {
            middlePanel.remove(showNumberOfBookLabel);
            
            

        }
        updatePage();
    } else if (!advancedFilterResult.isEmpty()) {
        bookList = advancedFilterResult;
        bookImageList = advancedImageResult;
        int response = JOptionPane.showOptionDialog(
            this, 
            "Filtered " + bookList.size() + " books.", 
            "Information", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            new Object[] {"OK"}, 
            "OK"
        );
        if (response == JOptionPane.OK_OPTION) {
            middlePanel.remove(showNumberOfBookLabel);
            
            
            
            advancedFilterDialog.dispose();
            

        }
        
        
        updatePage();
    } else {
        
        bookList = BookFileManager.getBookList();
        bookImageList = BookFileManager.getBookImageList();
        
        
        int response = JOptionPane.showOptionDialog(
            this, 
            "Filtered " + bookList.size() + " books.", 
            "Information", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            new Object[] {"OK"}, 
            "OK"
        );
        if (response == JOptionPane.OK_OPTION) {
            middlePanel.remove(showNumberOfBookLabel);
            
            
            
            advancedFilterDialog.dispose();
            

        }
            
        updatePage();
    }
}

private void updateCommonResults() {
    middlePanel.remove(showNumberOfBookLabel);
    
    filteredBooks.clear();
    filterBookImageList.clear();

    
    for (int i = 0; i < BookFileManager.getBookList().size(); i++) {
        Book book = BookFileManager.getBookList().get(i);
    
        if (normalSearchResult.contains(book) && advancedFilterResult.contains(book)) {
            filteredBooks.add(book);
            filterBookImageList.add(BookFileManager.getBookImageList().get(i));
        }
    }

    bookList = filteredBooks;
    bookImageList = filterBookImageList;
    currentPage = 0;

    int response = JOptionPane.showOptionDialog(
            this, 
            "Filtered " + bookList.size() + " books.", 
            "Information", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            new Object[] {"OK"}, 
            "OK"
        );
    if (response == JOptionPane.OK_OPTION) {
        middlePanel.remove(showNumberOfBookLabel);
        
        
        
        advancedFilterDialog.dispose();
        

    }
    updatePage();
    

}


private void resetAdvancedFilters() {
    languageBox.setSelectedIndex(0);
    genreBox.setSelectedIndex(0);
    minYearField.setText("");
    maxYearField.setText("");
    minPagesField.setText("");
    maxPagesField.setText("");
    if (people instanceof StaffData) {
        availabilityBox.setSelectedIndex(0);
        
    }
    
}


public void updatePage() {
    
    
    
    mainPanel.removeAll();
    
    int start = currentPage * BOOKS_PER_PAGE;
    
    int end = Math.min(start + BOOKS_PER_PAGE, bookList.size());
    


    int itemCount=0;
    
    for (int i = start; i < end; i++) {
        
        Book book = bookList.get(i);
        
        JPanel bookPanel = createBookListPanel(i + 1, bookImageList.get(i),
            book.getBookName(), book.getAuthor(), book.getIsbn(),
            book.getGenre(), book.getPublisher(), book.getLanguage(),
            book.getNumberofPage(), book.getPublicationYear(), book.isAvailable());
        mainPanel.add(bookPanel);
        itemCount++;
    }

    while (itemCount < BOOKS_PER_PAGE) {
        JPanel filler = new JPanel();
        filler.setPreferredSize(new Dimension(mainPanel.getWidth(), 120)); // Adjust height as needed
        mainPanel.add(filler);
        itemCount++;
    }

    
 
    prevButton.setEnabled(currentPage > 0);
    nextButton.setEnabled(end < bookList.size());
    
    updatePaginationPanel();

    paginationPanel.revalidate();
    paginationPanel.repaint();
    mainPanel.revalidate();
    mainPanel.repaint();
    
    scrollPane.revalidate();
    scrollPane.repaint();
    centerWrapperPanel.revalidate();
    centerWrapperPanel.repaint();
    this.revalidate();   // JFrame
    this.repaint();      // JFrame
    
    
    
}

private void updatePaginationPanel() {
    paginationPanel.removeAll();

    int totalPages = (int) Math.ceil((double) bookList.size() / BOOKS_PER_PAGE);

    
    int pageSize = 7;
    int halfWindow = pageSize / 2;

    int startPage = Math.max(0, currentPage - halfWindow);
    int endPage = Math.min(totalPages, startPage + pageSize);

    if (endPage - startPage < pageSize && startPage > 0) {
        startPage = Math.max(0, endPage - pageSize);
    }

    for (int i = startPage; i < endPage; i++) {
        int pageNum = i;
        JButton pageButton = new JButton(String.valueOf(pageNum + 1));
        pageButton.setFont(fieldFont);
        pageButton.setEnabled(pageNum != currentPage); // Disable current page button
        pageButton.addActionListener(e -> {
            currentPage = pageNum;
            middlePanel.remove(showNumberOfBookLabel);
            updatePage();
        });
        paginationPanel.add(pageButton);
    }

    int startResult = currentPage * BOOKS_PER_PAGE + 1;
    if(totalPages==0){
        startResult = currentPage * BOOKS_PER_PAGE;
    }
    int endResult = Math.min((currentPage + 1) * BOOKS_PER_PAGE, bookList.size());
    
    if(filterInfo==null){
        filterInfo.append("");
    }
    showNumberOfBookLabel = new JLabel("Results " + startResult + " to " + endResult + " of " + bookList.size() + " "+filterInfo);

    middlePanel.add(showNumberOfBookLabel, BorderLayout.SOUTH);
    
    paginationPanel.revalidate();
    paginationPanel.repaint();
    middlePanel.revalidate();
    middlePanel.repaint();
    mainPanel.setVisible(true);
    scrollPane.setVisible(true);
    centerWrapperPanel.setVisible(true);

    
}


private void changePage(int direction) {
    
    
    currentPage += direction;
    middlePanel.remove(showNumberOfBookLabel);
    updatePage();
    
}

private JLabel createHeaderLabel(String text, int width) {
    JLabel label = new JLabel(text);
    label.setFont(fieldFont);
    label.setPreferredSize(new Dimension(width, 30));
    label.setMaximumSize(new Dimension(width, 30));
    label.setMinimumSize(new Dimension(width, 30));
    label.setHorizontalAlignment(SwingConstants.CENTER);
    return label;
}

public JPanel createBookListPanel(int index, String imageFilePath, String bookName, String author, String isbn,
                               String genre, String publisher, String language, int numberOfPage, 
                               int publisherYear, boolean isAvailable) {
    
    
    

    
    JPanel bookPanel = new JPanel();
    bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.X_AXIS));
    bookPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    bookPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));


    JPanel leftPanel = new JPanel();
    leftPanel.setPreferredSize(new Dimension(40, 150));
    leftPanel.setMaximumSize(new Dimension(40, 150));
    leftPanel.setMinimumSize(new Dimension(40, 150));
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
    JLabel numberLabel = new JLabel(String.valueOf(index));
    numberLabel.setFont(new Font("SimSun", Font.BOLD, 16));
    numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    leftPanel.add(numberLabel);
    bookPanel.add(leftPanel);
    bookPanel.add(createVerticalSeparator());
    

    JPanel centerPanel = new JPanel();
    centerPanel.setPreferredSize(new Dimension(270, 200));
    centerPanel.setMaximumSize(new Dimension(270, 200));
    centerPanel.setMinimumSize(new Dimension(270, 200));
   
    JLabel bookImage = new JLabel(); 
    

    File file = new File(BookFileManager.getBookImageFile(), imageFilePath);  // Combine folder path and image file name
    

    try {
        
        BufferedImage img = ImageIO.read(file);
        
        
        if (img != null && img.getWidth() > 0 && img.getHeight() > 0) {
            bookImage.setIcon(new ImageIcon(img));  // Set the icon with an ImageIcon
        } else {
            bookImage.setText("Image not available");
        }
    } catch (Exception e) {
        System.out.println("Failed to load image: " + file);
    }
    
    centerPanel.add(bookImage);
    bookPanel.add(centerPanel);
    bookPanel.add(createVerticalSeparator());
   


    JPanel bookInfoPanel = new JPanel();
    bookInfoPanel.setPreferredSize(new Dimension(500, 150));
    bookInfoPanel.setMaximumSize(new Dimension(500, 150));
    bookInfoPanel.setMinimumSize(new Dimension(500, 150));
    

    bookInfoPanel.setLayout(new BoxLayout(bookInfoPanel, BoxLayout.Y_AXIS));
    JLabel titleLabel = new JLabel("Title: "+bookName);
    JLabel isbnLabel = new JLabel("ISBN: " +isbn);
    JLabel languageLabel = new JLabel("Language: "+language );
    JLabel genreLabel = new JLabel("Genre: "+genre );
    JLabel pageLabel = new JLabel("Page: "+numberOfPage );

    formatCenteredLabel(titleLabel);
 
    formatCenteredLabel(isbnLabel);
 
    formatCenteredLabel(languageLabel);
 
    formatCenteredLabel(genreLabel);
 
    formatCenteredLabel(pageLabel);
 
    titleLabel.setFont(fieldFont);
    isbnLabel.setFont(fieldFont);
    languageLabel.setFont(fieldFont);
    genreLabel.setFont(fieldFont);
    pageLabel.setFont(fieldFont);
    
    bookInfoPanel.add(titleLabel);
    bookInfoPanel.add(isbnLabel);
    bookInfoPanel.add(languageLabel);
    bookInfoPanel.add(genreLabel);
    bookInfoPanel.add(pageLabel);

    bookPanel.add(bookInfoPanel);
    bookPanel.add(createVerticalSeparator());




    JPanel authorAndPublisherInfoPanel = new JPanel();
    authorAndPublisherInfoPanel.setPreferredSize(new Dimension(420, 150));
    authorAndPublisherInfoPanel.setMaximumSize(new Dimension(420, 150));
    authorAndPublisherInfoPanel.setMinimumSize(new Dimension(420, 150));

   
    authorAndPublisherInfoPanel.setLayout(new BoxLayout(authorAndPublisherInfoPanel, BoxLayout.Y_AXIS));
    JLabel authorLabel = new JLabel("Author: "+author );
    JLabel publisherLabel = new JLabel("Publisher: "+publisher);
    JLabel yearLabel = new JLabel("Year: "+publisherYear);

    formatCenteredLabel(authorLabel);
    formatCenteredLabel(publisherLabel);
    formatCenteredLabel(yearLabel);
    
    authorLabel.setFont(fieldFont);
    publisherLabel.setFont(fieldFont);
    yearLabel.setFont(fieldFont);
    
    
    authorAndPublisherInfoPanel.add(authorLabel);
    authorAndPublisherInfoPanel.add(publisherLabel);
    authorAndPublisherInfoPanel.add(yearLabel);
    

    bookPanel.add(authorAndPublisherInfoPanel);
    bookPanel.add(createVerticalSeparator());


    if (people instanceof StaffData) {
        JPanel availabilityPanel = new JPanel();
        availabilityPanel.setPreferredSize(new Dimension(200, 150));
        availabilityPanel.setMaximumSize(new Dimension(200, 150));
        availabilityPanel.setMinimumSize(new Dimension(200, 150));

    
        availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.Y_AXIS));
        JLabel availabilityLabel = new JLabel("Availability: "+(isAvailable?"Available":"Unavailable"));
        
        
        availabilityLabel.setFont(fieldFont);
        
        
        
        availabilityPanel.add(availabilityLabel);
        

        bookPanel.add(availabilityPanel);
   

        
    }

    if (people instanceof UserData) {
        JPanel actionPanel = new JPanel();
        actionPanel.setPreferredSize(new Dimension(150, 150));
        actionPanel.setMaximumSize(new Dimension(150, 150));
        actionPanel.setMinimumSize(new Dimension(150, 150));

    
        actionPanel.setLayout(new GridBagLayout());

        JButton reserveButton = new JButton("Reserve");
        reserveButton.setFont(fieldFont);
        
        
        
        actionPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        actionPanel.add(reserveButton);

                
        bookPanel.add(actionPanel);


        reserveButton.addActionListener(e -> {
            boolean isFromReturnBook=false;
            new ReserveBookManager(bookList,borrowedList,bookImageList,this.currentUser,index,isFromReturnBook);
        
        });

    }
    


    

    
    
    
    return bookPanel;
}


private void formatCenteredLabel(JLabel label) {
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

   
}


}




