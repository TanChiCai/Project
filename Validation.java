public class Validation {

    // Method to validate phone number format (user-specific)
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Both formats: 011-111-1111 or 011-1111-1111
        String regex = "^01[1-9]-[0-9]{3,4}-[0-9]{4}$";
        return phoneNumber.matches(regex);
    }

    // Method to validate password format (generic for both user and staff)
    public static boolean isValidPassword(String password) {
        return password.length() >= 8 && 
               password.matches(".*[A-Za-z].*") && 
               password.matches(".*[0-9].*") && 
               password.matches(".*[!@#$%^&*()].*");
    }

    // Method to check if passwords match (generic for both user and staff)
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    // Method to validate username (for user)
    public static boolean isValidUsername(String username) {
        // Example: username length between 4 and 35 characters and no special characters
        return username.length() >= 4 && username.length() <= 35 && username.matches("[a-zA-Z0-9_]+");
    }
}
