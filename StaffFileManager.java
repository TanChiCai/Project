import java.io.*;

public class StaffFileManager extends FileManagement {
    private static final String STAFF_FILE_NAME = "staffs.txt";

    public StaffFileManager() {}

    
    public StaffData validateStaffLogin(String staffID, String password) {
        try (BufferedReader reader = getFileReader(STAFF_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 3 && data[0].equals(staffID) && data[2].equals(password)) {
                    StaffData staff = new StaffData();
                    staff.setStaffid(staffID);
                    staff.setName(data[1]);
                    staff.setPassword(data[2]);
                    staff.setPhoneNumber(data[3]);
                    return staff;
                }
            }
        } catch (IOException e) {
            System.out.println("Error validating staff login: " + e.getMessage());
        }
        return null;
    }
    

    public static boolean isStaffExists(String staffID) {
        try (BufferedReader reader = getFileReader(STAFF_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 1 && data[0].equals(staffID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking staff existence: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkStaffPhoneNumber(String staffID, String phone) {
        try (BufferedReader reader = getFileReader(STAFF_FILE_NAME)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4) {
                    String storedStaffID = data[0].trim();
                    String storedStaffPhone = data[3].trim();
                    if (storedStaffID.equals(staffID) && storedStaffPhone.equals(phone)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking phone number: " + e.getMessage());
        }
        return false;
    }

    public static boolean updateStaffPassword(String staffID, String newPassword) {
        File file = new File(STAFF_FILE_NAME);
        StringBuilder updatedContent = new StringBuilder();
        boolean staffFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(SEPARATOR);
                if (data.length >= 4 && data[0].equals(staffID)) {
                    updatedContent.append(data[0]).append(SEPARATOR)
                                  .append(data[1]).append(SEPARATOR)
                                  .append(newPassword).append(SEPARATOR)
                                  .append(data[3]).append("\n");
                    staffFound = true;
                } else {
                    updatedContent.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading staff file: " + e.getMessage());
            return false;
        }

        if (staffFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(updatedContent.toString());
                return true;
            } catch (IOException e) {
                System.out.println("Error writing updated staff file: " + e.getMessage());
                return false;
            }
        }

        return false;
    }
}
