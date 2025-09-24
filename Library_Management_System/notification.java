import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class notification extends JFrame{

    private JLabel label;
	private JTable table;
    private DefaultTableModel tableList;
    private static ArrayList<UserData> usersList = new ArrayList<>();;
    private static List<BlackListDetail> blacklistedUsers = new ArrayList<>();;
    
    public notification(List<BlackListDetail> blackList, UserData currentUser) {
        setTitle("Notification");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);
        setLayout(null);
        int screenW = screenSize.width;
		int screenH = screenSize.height;

        label = new JLabel("Notification");
        label.setFont(new Font("Arial", Font.BOLD, 80));
        int labelW = 500;
        int labelH = 150;
        int xL = (screenW - labelW) / 2;
        label.setBounds(xL, 10, labelW, labelH);

        String[] columns = {"No.", "Notice"};
        tableList = new DefaultTableModel(columns, 0){
        	@Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        };

        table = new JTable(tableList);
    	JScrollPane Table = new JScrollPane(table);
        table.setRowHeight(25);
        table.setFont(new Font("Calibri Light", Font.BOLD, 20));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(1150);
        table.getTableHeader().setReorderingAllowed(false);

        int tableW = 1200;
        int tableH = screenH / 2;
        int xT = (screenW - tableW) / 2;
        int yT = (screenH - tableH) / 2;
        Table.setBounds(xT, yT, tableW, tableH);

        boolean isBlacklisted = false;
        for (BlackListDetail blackListInfo : blackList) {
            if (blackListInfo.getUser().getUserid().equals(currentUser.getUserid())) {
                isBlacklisted = true;
                break;
            }
        }

        if (isBlacklisted) {
            Object[] row = {1, "Sorry, you have been blacklisted. You can't borrow or reserve books."};
            tableList.addRow(row);
            add(Table);
        } else {
            JLabel noDataLabel = new JLabel("You don't have any notification");
		    noDataLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		    noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		    noDataLabel.setBounds(0, screenH / 2 - 50, screenW, 100);
		    add(noDataLabel);
        }

        add(label);
        setVisible(true);
    }

    public static List<BlackListDetail> loadBlackList(String filename) {
        List<BlackListDetail> blacklistedUsers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 4) {
                    String userId = parts[0];
                    boolean isBlacklisted = Boolean.parseBoolean(parts[3]);
                    
                    if (isBlacklisted) {
                        UserData user = new UserData(userId, parts[1], "", parts[2]);
                        BlackListDetail blackList = new BlackListDetail(user, true, parts.length > 4 ? parts[4] : "-");
                        blacklistedUsers.add(blackList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blacklistedUsers;
    }

    public static UserData findUserById(String userid) {
		ArrayList<UserData> userList = new ArrayList<>();
		userList=UserFileManager.loadAllUsers();
	    for (UserData user : userList) {
	        if (user.getUserid().equals(userid)) {
	            return user; 
	        }
	    }
	    return null; 
	}
}