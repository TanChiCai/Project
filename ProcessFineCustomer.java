import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcessFineCustomer extends JFrame{
	
	private JLabel label;
	private JTable table;
    private DefaultTableModel tableList;
    private String formatDate(LocalDate date) {
	    return (date == null) ? "-" : date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	private static ArrayList<UserData> usersList = new ArrayList<>();;
    
	public ProcessFineCustomer(List<ProcessFineDetail> processFine, UserData currentUser){
		setTitle("ProcessFine");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        setResizable(false);
        setLayout(null);
        int screenW = screenSize.width;
		int screenH = screenSize.height;
		
		label = new JLabel("Process Fine");
        label.setFont(new Font("Arial", Font.BOLD, 80));
        int labelW = 500;
        int labelH = 150;
        int xL = (screenW - labelW) / 2;
        label.setBounds(xL, 10, labelW, labelH);// Position (x, y), size (w, h)
        
        String[] columns = {"Process Find ID", "User ID", "Book ID", "Book Name", "Payment", "Pay Date", "Due Date", "Pay Status", "Pay Button"};
        tableList = new DefaultTableModel(columns, 0){
        	@Override
            public boolean isCellEditable(int row, int column) {
                // Only the last column (button column) is editable (clickable button)
                return column == 8; // The last column (button)
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
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.getTableHeader().setReorderingAllowed(false);//fixed columns
        
        // Add button column as a JButton
        table.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), processFine));
        
        int tableW = 1200;
        int tableH = screenH / 2;
        int xT = (screenW - tableW) / 2;
        int yT = (screenH - tableH) / 2;
        Table.setBounds(xT, yT, tableW, tableH);// Position (x, y), size (w, h)
        
        setUpComboBoxEditor();
        
        int matchingRows = 0;
		for (ProcessFineDetail fine : processFine) {
			if (fine.getUser().getUserid().equals(currentUser.getUserid())){
				Object[] row = {
					fine.getProcessFindID(),
					fine.getUser().getUserid(),
					fine.getbook().getBookID(),
					fine.getbook().getBookName(),
					fine.getPayment(),
					formatDate(fine.getPayDate()),
					formatDate(fine.getFineDueDate()),
					fine.isPayStatus() ? "Complete" : "Incomplete",
					"Pay"
				};
			
				tableList.addRow(row);
				matchingRows++;
			}
		}
		
		add(label);
		
		if (matchingRows == 0) {
		    JLabel noDataLabel = new JLabel("You don't have any process fine");
		    noDataLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		    noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    noDataLabel.setBounds(0, screenH / 2 - 50, screenW, 100);
		    add(noDataLabel);
		} else {
		    add(Table);
		}
		
		setVisible(true);
	}
	
	// Custom button renderer for JTable cells
    class ButtonRenderer extends JButton implements TableCellRenderer {
	    public ButtonRenderer() {
	        setOpaque(true);
	    }
	
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	                                                   boolean isSelected, boolean hasFocus,
	                                                   int row, int column) {
	        setText((value == null) ? "Pay" : value.toString());
	
	        // Check pay status in the same row, column 7
	        String payStatus = table.getValueAt(row, 7).toString();
	        setEnabled("Incomplete".equalsIgnoreCase(payStatus));
	
	        return this;
	    }
	}

    // Custom button editor for JTable cells
	class ButtonEditor extends DefaultCellEditor {
	    protected JButton button;
	    private String label;
		private boolean isPushed;
	    private JTable table;
	    private List<ProcessFineDetail> processFineList;

		public ButtonEditor(JCheckBox checkBox, List<ProcessFineDetail> processFineList) {
	        super(checkBox);
	        button = new JButton();
	        button.setOpaque(true);
	
	        button.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            String payStatus = table.getValueAt(row, 7).toString();
	
	            if (!"Incomplete".equalsIgnoreCase(payStatus)) {
	                return; // Do not proceed if already complete
	            }
	
	            fireEditingStopped();
	
	            // Assume column 5 holds the payment value (adjust if needed)
	            Object paymentValue = table.getValueAt(row, 4);
	
	            // Show the payment dialog with just a JLabel
	            JDialog dialog = new JDialog((Frame) null, "Payment Details", false);
	            dialog.setLayout(new BorderLayout());
	            
	            JPanel labelPanel = new JPanel();
				labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
	
	            JLabel label = new JLabel("Payment Value: RM" + paymentValue);
	            label.setFont(new Font("Arial", Font.BOLD, 30));
	            JLabel label2 = new JLabel("Please choose a payment method");
	            label2.setFont(new Font("Arial", Font.BOLD, 30));
	            label.setHorizontalAlignment(SwingConstants.CENTER);
	            label.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
	            label2.setHorizontalAlignment(SwingConstants.CENTER);
	            label2.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
	            // Add labels to the panel
				labelPanel.add(label);
				labelPanel.add(label2);
				// Add the panel to the top of the dialog
				dialog.add(labelPanel, BorderLayout.NORTH);
	
	            // Payment method buttons panel
				JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 20));
				
				String[] methods = {"Credit/Debit Card", "Online Banking", "eWallet"};
				for (String method : methods) {
				    JButton methodButton = new JButton(method);
				    methodButton.setPreferredSize(new Dimension(200, 30));
    				methodButton.setFont(new Font("Arial", Font.BOLD, 16));
				    methodButton.addActionListener(ev -> {
				        // Confirm payment method selection
				        JOptionPane.showMessageDialog(dialog, method + " payment completed successfully.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
				        
				        // Update table and data
				        table.setValueAt("Complete", table.getSelectedRow(), 7); // Pay Status
				        LocalDate today = LocalDate.now();
				        String formattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				        table.setValueAt(formattedDate, table.getSelectedRow(), 5); // Pay Date
				
				        // Update in memory
				        String processID = table.getValueAt(row, 0).toString();
				        for (ProcessFineDetail fine : processFineList) {
				            if (fine.getProcessFindID().equals(processID)) {
				                fine.setPayStatus(true);
				                fine.setPayDate(today);
				                break;
				            }
				        }
				
				        // Save to file
				        saveProcessFineToFile("processFine.txt", processFineList);
				        dialog.dispose();
				    });
				    buttonPanel.add(methodButton);
				}
				
				dialog.add(buttonPanel, BorderLayout.SOUTH);
	
	            dialog.setSize(700, 240);
	            dialog.setLocationRelativeTo(button);
	            dialog.setVisible(true);
	        });
	    }
	    
	    public static void saveProcessFineToFile(String filename, List<ProcessFineDetail> data) {
		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
		        for (ProcessFineDetail fine : data) {
		            // Convert null payDate to "-" before saving
		            String payDate = (fine.getPayDate() == null) ? "-" : fine.getPayDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		            // Convert the fine's due date to string, assuming it's not null
		            String fineDueDate = fine.getFineDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		            // Write to file in the tab-separated format
		            writer.write(fine.getProcessFindID() + "\t" +
		                         fine.getUser().getUserid() + "\t" +
								 fine.getbook().getBookID() + "\t" +
		                         fine.getbook().getBookName() + "\t" +
		                         fine.getPayment() + "\t" +
		                         payDate + "\t" +
		                         fineDueDate + "\t" +
		                         fine.isPayStatus());
		            writer.newLine();
		        }
		    } catch (IOException e) {
		        JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
		    }
		}
	    
	    public Component getTableCellEditorComponent(JTable table, Object value,
	                                                 boolean isSelected, int row, int column) {
	        this.table = table;
	        label = (value == null) ? "Pay" : value.toString();
	        button.setText(label);
	
	        // Disable the button if status is not complete
	        String payStatus = table.getValueAt(row, 7).toString();
	        button.setEnabled("Incomplete".equalsIgnoreCase(payStatus));
	
	        isPushed = true;
	        return button;
	    }
	
	    @Override
	    public Object getCellEditorValue() {
	        isPushed = false;
	        return label;
	    }
	}
	
	private void setUpComboBoxEditor() {
        // PayStatus
        JComboBox<String> payStatusCombo = new JComboBox<>(new String[]{"Complete","Incomplete"});
        TableColumn payStatusColumn = table.getColumnModel().getColumn(7);
        payStatusColumn.setCellEditor(new DefaultCellEditor(payStatusCombo));
    }
    
    public static List<ProcessFineDetail> loadProcessFineFromFile(String filename) {
        List<ProcessFineDetail> processFine = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 8) {
                	String ProcessFindID = parts[0];
			    	String userid = parts[1];
					String bookID = parts[2];
			        String bookName = parts[3];
			        int payment = 0;
					try {
						if (!parts[4].equals("-")) {
							payment = Integer.parseInt(parts[4]);
						}
					} catch (NumberFormatException e) {
						System.err.println("Invalid payment value: " + parts[4]);
					}
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

	public static UserData findUserById(String userid, String name, String phoneNumber) {
		ArrayList<UserData> userList = new ArrayList<>();
		userList=UserFileManager.loadAllUsers();
		for (UserData user : userList) {
			if (user.getUserid().equals(userid) && user.getName().equals(name) && user.getPhoneNumber().equals(phoneNumber)) {
				return user; // Return the matching UserData object
			}
		}
		return null; // Return null if no match is found
	}
	
	
}