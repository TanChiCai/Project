import java.io.*;

public class StaffData extends Data {
    private String staffid;
    private static final String ID_START = "S";
    private static final String ID_FILE = "staffid_counter.txt";

    public StaffData() {}

    public StaffData(String staffid,String name, String password, String phoneNumber) {
        super(name, password, phoneNumber);
        this.staffid = staffid;
    }

    public String getStaffid() {
        return staffid;
    }

    public void setStaffid(String staffid) {
        this.staffid = staffid;
    }

    


    public static String generateStaffid() {
        try {
            File file = new File(ID_FILE);
            String lastId = "U0000";

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (line != null) lastId = line;
                }
            }

            int lastNumber = Integer.parseInt(lastId.substring(3));
            int newNumber = lastNumber + 1;
            String newId = ID_START + String.format("%06d", newNumber);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(newId);
            }

            return newId;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error generating staff ID: " + e.getMessage());
            return ID_START + "0001";
           
        }
    }

    @Override
    public String toString() {
        return staffid + super.toString();
    }
}
