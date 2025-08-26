import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlackList extends JFrame {
	
	private JLabel label;
    private JButton saveButton;
    private JTable table;
    private DefaultTableModel tableModel;

    public BlackList(List<BlackListDetail> Blacklist) {
    	setTitle("Black List");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // center the window
        setResizable(false);
        setLayout(null);
        int screenW = screenSize.width;
		int screenH = screenSize.height;
        
        label = new JLabel("Black List");
        label.setFont(new Font("Arial", Font.BOLD, 80));
        int labelW = 400;
        int labelH = 150;
        int xL = (screenW - labelW) / 2;
        label.setBounds(xL, 10, labelW, labelH);// Position (x, y), size (w, h)
        
        String[] columns = {"User ID", "Name", "Phone Number", "Blacklisted", "Reason"};
        tableModel = new DefaultTableModel(columns, 0) {
        	public boolean isCellEditable(int row, int column) {
		        // "Blacklisted" column is always editable
		        if (column == 3) return true;
		        // "Reason" column is only editable when blacklist column is "Black List"
		        if (column == 4) {
		            Object blacklistValue = getValueAt(row, 3);
		            return "Black List".equals(blacklistValue);
		        }
		        return false;
		    }
		};
		
		tableModel.addTableModelListener(e -> {
		    int row = e.getFirstRow();
		    int column = e.getColumn();
		
		    // When the blacklist column changes
		    if (column == 3) {
		        Object value = tableModel.getValueAt(row, column);
		        if ("-".equals(value)) {
		            // Set reason to "null" and refresh the table view
		            tableModel.setValueAt("-", row, 4);
		        }else if ("Black List".equals(value)){
		        	tableModel.setValueAt("reason", row, 4);
		        }
		    }
		});
		
        table = new JTable(tableModel);
        JScrollPane Table = new JScrollPane(table);
        int tableW = 1200;
        int tableH = screenH / 2;
        int xT = (screenW - tableW) / 2;
        int yT = (screenH - tableH) / 2;
        Table.setBounds(xT, yT, tableW, tableH);// Position (x, y), size (w, h)
        table.setRowHeight(25);
        table.setFont(new Font("Calibri Light", Font.BOLD, 20));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(500);
        table.getTableHeader().setReorderingAllowed(false);//fixed columns
        
        // Add custom editors (dropdowns)
        setUpComboBoxEditors();
        
        for (int i = 0; i < Blacklist.size(); i++) {
		    BlackListDetail blackList = Blacklist.get(i);
            
		    Object[] row = {
		    	blackList.getUser().getUserid(),
		        blackList.getUser().getName(),
		        blackList.getUser().getPhoneNumber(),
		        String.valueOf(blackList.isBlacklisted() ? "Black List" : "-"),
		        blackList.getReason()
		    };
		
		    tableModel.addRow(row);
		}
        
        saveButton = new JButton("Save");
        int buttonW = 100;
        int buttonH = 40;
        int xB = (screenW - buttonW) / 2;
        int yB = (screenH - buttonH) * 4 / 5;
        saveButton.setBounds(xB, yB, buttonW, buttonH);
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //When button clicked, run save class
                saveBlacklist save = new saveBlacklist();
                save.runTask(tableModel);
                showMessage("Save completely!");
            }        	
        });
        
        add(label);
        add(Table);
        add(saveButton);
        setVisible(true);
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setUpComboBoxEditors() {
        // Blacklisted dropdown (true/false)
        JComboBox<String> blacklistCombo = new JComboBox<>(new String[]{"Black List","-"});
        TableColumn blacklistColumn = table.getColumnModel().getColumn(3);
        blacklistColumn.setCellEditor(new DefaultCellEditor(blacklistCombo));

        // Reason dropdown
        JComboBox<String> reasonCombo = new JComboBox<>(new String[]{"No pay process fine.", "No return book long time.","Other"});
        TableColumn reasonColumn = table.getColumnModel().getColumn(4);
        reasonColumn.setCellEditor(new DefaultCellEditor(reasonCombo));
        
    }

    
    
    public static List<BlackListDetail> loadBlacklist(String filename) {
        List<BlackListDetail> Blacklist = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 5) {
                    String userid = parts[0];
                    String name = parts[1];
                    String phoneNumber = parts[2];
                    boolean blacklist = Boolean.parseBoolean(parts[3]);
                    String reason = parts[4];
                    UserData user = findUserById(userid, name, phoneNumber);
    
                    Blacklist.add(new BlackListDetail(user, blacklist, reason));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Blacklist;
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