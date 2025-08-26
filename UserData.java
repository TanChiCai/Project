import java.io.*;
import java.util.Objects;

public class UserData extends Data {
    private String userid;
    private static final String ID_START = "U";
    private static final String ID_FILE = "userid_tracker.txt";
    private boolean reservationMade;

    public UserData() {}

    public UserData(String userid) {
        this.userid = userid;
    }
    
    public UserData(String userid,String name, String password, String phoneNumber) {
        super(name, password, phoneNumber);
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static String generateUserid() {
        try {
            File file = new File(ID_FILE);
            String lastId = "U0000";

            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    if (line != null && line.startsWith(ID_START)) {
                        lastId = line.trim();
                    }
                } catch (IOException e) {
                    System.out.println("Error reading ID file: " + e.getMessage());
                }
            }

            int lastNumber;
            try {
                lastNumber = Integer.parseInt(lastId.substring(1)); // Remove "U" prefix
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Resetting to 0.");
                lastNumber = 0;
            }

            int newNumber = lastNumber + 1;
            String newId = ID_START + String.format("%04d", newNumber);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(newId);
            }

            return newId;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error generating user ID: " + e.getMessage());
            return ID_START + "0001";
        }
    }

     public boolean getReservationMade() {
        return reservationMade;
    }

    public void setReservationMade(boolean reservationMade) {
        this.reservationMade = reservationMade;
    }

    @Override
    public String toString() {
        return userid + super.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        UserData otherUser = (UserData) obj;
        
        return this.userid.equals(otherUser.userid);
    }

    public int hashCode() {
        return Objects.hash(userid); // Same fields as in equals()
    }

}
