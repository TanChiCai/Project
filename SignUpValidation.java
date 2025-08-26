public class SignUpValidation extends Validation {

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return Validation.isValidPhoneNumber(phoneNumber);  // Calls the parent method
    }

    // Method to check if the passwords match (same as the parent class)
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return Validation.doPasswordsMatch(password, confirmPassword);  // Reusing parent class method
    }

    // Method to validate the password format (using parent class method)
    public static boolean isValidPassword(String password) {
        return Validation.isValidPassword(password);  // Reusing parent class method
    }

    // Custom method for sign-up to validate password length and complexity
    public static boolean isPasswordComplexEnough(String password) {
        return password.length() >= 8 && password.matches(".*[A-Za-z].*") && 
               password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*()].*");
    }
}
