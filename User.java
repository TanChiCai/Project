import java.io.*;

public class User {

    // Empty constructor (good OOP practice)
    public User() {}

    // Register a new user using UserData object
    public static boolean registerUser(UserData newUser) throws IOException {
        if (isUsernameExists(newUser.getName())) {
            return false;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));
        writer.write(newUser.getName() + "\t" + newUser.getPassword() + "\t" + newUser.getPhoneNumber());
        writer.newLine();
        writer.close();
        return true;
    }

    // Check if the username already exists
    public static boolean isUsernameExists(String username) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            if (parts.length > 0 && parts[0].equalsIgnoreCase(username)) {
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }
}
