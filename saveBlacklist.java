import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.List;

public class saveBlacklist {

    public void runTask(DefaultTableModel tableModel) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Blacklist.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String userid = (String) tableModel.getValueAt(i, 0);
                String name = (String) tableModel.getValueAt(i, 1);
                String phoneNumber = (String) tableModel.getValueAt(i, 2);
                String blacklist = (String) tableModel.getValueAt(i, 3);
                String reason = (String) tableModel.getValueAt(i, 4);

                // Convert "Black List"/"-" to true/false
                boolean isBlacklisted = "Black List".equals(blacklist);

                writer.write(userid + "\t" + name + "\t" + phoneNumber + "\t" + isBlacklisted + "\t" + reason);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
