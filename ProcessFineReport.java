import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessFineReport extends JFrame{
	
	private JLabel label;
	private JTable table;
    private DefaultTableModel tableList;
    private String formatDate(LocalDate date) {
	    return (date == null) ? "-" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

    
	public ProcessFineReport(List<ProcessFineDetail> processFine){
		setTitle("ProcessFine");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null); // center the window
        setResizable(false);
        setLayout(null);
        int screenW = screenSize.width;
		int screenH = screenSize.height;
		
		label = new JLabel("Process Fine Monthly Report for May");
        label.setFont(new Font("Arial", Font.BOLD, 50));
        int labelW = 900;
        int labelH = 150;
        int xL = (screenW - labelW) / 2;
        label.setBounds(xL, 10, labelW, labelH);// Position (x, y), size (w, h)
        
        String[] columns = {"Process Find ID", "User ID", "Book ID", "Book Name", "Payment", "Pay Date", "Due Date", "Pay Status"};
        tableList = new DefaultTableModel(columns, 0){
        	@Override
            public boolean isCellEditable(int row, int column) {
                // Only the last column (button column) is editable (clickable button)
                return column == 7; // The last column (button)
            }
        };
        
    	table = new JTable(tableList);
    	JScrollPane Table = new JScrollPane(table);
        table.setRowHeight(25);
        table.setFont(new Font("SimSun", Font.BOLD, 20));
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(140);
        table.getTableHeader().setReorderingAllowed(false);//fixed columns
        
        int tableW = 1120;
        int tableH = screenH / 2;
        int xT = (screenW - tableW) / 2;
        int yT = (screenH - tableH) / 2;
        Table.setBounds(xT, yT, tableW, tableH);// Position (x, y), size (w, h)
        
        setUpComboBoxEditor();
        int matchingRows = 0;
		for (ProcessFineDetail fine : processFine) {
            if (fine.isPayStatus()==false) {
                Object[] row = {
                    fine.getProcessFindID(),
                    fine.getUser().getUserid(),
                    fine.getbook().getBookID(),
                    fine.getbook().getBookName(),
                    fine.getPayment(),
                    formatDate(fine.getPayDate()),
                    formatDate(fine.getFineDueDate()),
                    fine.isPayStatus() ? "Complete" : "Incomplete",
                };
            
                tableList.addRow(row);
                matchingRows++;
            }
		}
		
		add(label);
		
		if (matchingRows == 0) {
		    JLabel noDataLabel = new JLabel("No users have any process fine");
		    noDataLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		    noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    noDataLabel.setBounds(0, screenH / 2 - 50, screenW, 100);
		    add(noDataLabel);
		} else {
		    add(Table);
		}
		
		setVisible(true);
	}
	
	private void setUpComboBoxEditor() {
        // PayStatus
        JComboBox<String> payStatusCombo = new JComboBox<>(new String[]{"Complete","Incomplete"});
        TableColumn payStatusColumn = table.getColumnModel().getColumn(7);
        payStatusColumn.setCellEditor(new DefaultCellEditor(payStatusCombo));
    }
    
    public static List<ProcessFineDetail> loadProcessFineFromFile(String filename) {
        List<ProcessFineDetail> processFine = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 8) {
                	String ProcessFindID = parts[0];
			    	String userid = parts[1];
					String bookID = parts[2];
			        String bookName = parts[3];
			        int payment = Integer.parseInt(parts[4]); // Convert String to int
					LocalDate payDate = parts[5].equals("-") ? null : LocalDate.parse(parts[5], formatter);
					LocalDate fineDueDate = LocalDate.parse(parts[6], formatter);
		        	boolean payStatus = Boolean.parseBoolean(parts[7]);
		        	UserData user = findUserById(userid);
		        	Book book = findBook(bookID, bookName);
					

                    processFine.add(new ProcessFineDetail(ProcessFindID, user, book, payment, payDate, fineDueDate, payStatus));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load file: " + e.getMessage());
        }
        return processFine;
    }

	public static UserData findUserById(String userid) {
		ArrayList<UserData> userList = new ArrayList<>();
		userList=UserFileManager.loadAllUsers();
	    for (UserData user : userList) {
	        if (user.getUserid().equals(userid)) {
	            return user; // Return the matching UserData object
	        }
	    }
	    return null; // Return null if no match is found
	}
	
	public static Book findBook(String bookID, String bookName) {
		for (Book book : BookFileManager.getBookList()) {
			
			if (book.getBookID().equalsIgnoreCase(bookID) && book.getBookName().equalsIgnoreCase(bookName)) {
				
				return book; // Return the matching UserData object
			} 
		}
		return null; // Return null if no match is found
	}
	

}