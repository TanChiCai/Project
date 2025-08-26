import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserFileManager extends FileManagement {
    private static final String USER_FILE_NAME = "users.txt";
    private static ArrayList<UserData> usersList = new ArrayList<>();

    public UserFileManager() {}

    public static ArrayList<UserData> getUserList(){
        return usersList;
    }

    public static boolean saveUser(UserData user) {
        try (BufferedWriter writer = getFileWriter(USER_FILE_NAME, true)) {
            writer.write(user.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }

    public static boolean isPhoneNumberExist(String phoneNumber) {
        try (BufferedReader reader = getFileReader(USER_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4 && data[3].equals(phoneNumber)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking phone number existence: " + e.getMessage());
        }
        return false;
    }

    public UserData validateUserLogin(String username, String password) {
        try (BufferedReader reader = getFileReader(USER_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 3 && data[1].equals(username) && data[2].equals(password)) {
                    UserData user = new UserData();
                    user.setUserid(data[0]);
                    user.setName(data[1]);
                    user.setPassword(data[2]);
                    user.setPhoneNumber(data[3]);
                    return user;
                }
            }
        } catch (IOException e) {
            System.out.println("Error validating login: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateUserPassword(String username, String newPassword) {
        File file = new File(USER_FILE_NAME);
        StringBuilder updatedContent = new StringBuilder();
        boolean userFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4 && data[1].equals(username)) {
                    updatedContent.append(data[0]).append(SEPARATOR)
                                  .append(data[1]).append(SEPARATOR)
                                  .append(newPassword).append(SEPARATOR)
                                  .append(data[3]).append("\n");
                    userFound = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating password: " + e.getMessage());
            return false;
        }

        if (userFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(updatedContent.toString());
                return true;
            } catch (IOException e) {
                System.out.println("Error writing updated content: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    public static ArrayList<UserData> loadAllUsers() {
        
        try (BufferedReader reader = getFileReader(USER_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4) {
                    UserData user = new UserData();
                    user.setUserid(data[0]);
                    user.setName(data[1]);
                    user.setPassword(data[2]);
                    user.setPhoneNumber(data[3]);
                    usersList.add(user);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return usersList;
    }

    public static boolean checkUserPhoneNumber(String username, String phone) {
        try (BufferedReader reader = getFileReader(USER_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4) {
                    String storedUsername = data[1];
                    String storedPhone = data[3];
                    if (storedUsername.equals(username) && storedPhone.equals(phone)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking phone number: " + e.getMessage());
        }
        return false;
    }

    public static boolean isUserExists(String username) {
        try (BufferedReader reader = getFileReader(USER_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 2 && data[1].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking if user exists: " + e.getMessage());
        }
        return false;
    }

    public static Map<String, UserData> loadUserMap() {
        Map<String, UserData> userMap = new HashMap<>();

        for (UserData user : loadAllUsers()) {
            userMap.put(user.getUserid(), user);
        }

        return userMap;
    }

}
