import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class User_ForgotPasswordPage {

    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private String username;
    private String verificationCode;
    private JLabel codeLabel;

    public User_ForgotPasswordPage() {
        frame = new JFrame("Forgot Password");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createInputPanel(), "InputPage");
        mainPanel.add(createVerificationPanel(), "VerificationPage");
        mainPanel.add(createNewPasswordPanel(), "NewPasswordPage");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(220, 240, 255));

        JLabel titleLabel = GUI.createLabel("Forgot Password?", 130, 10, 200, 30, 20);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 60, 100, 25);
        panel.add(userLabel);

        JTextField userField = GUI.createPlaceholderTextField("Enter username", 150, 60, 200, 25);
        panel.add(userField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(50, 100, 100, 25);
        panel.add(phoneLabel);

        JTextField phoneField = GUI.createPlaceholderTextField("Enter 01x-xxx-xxxx or 01x-xxxx-xxxx", 150, 100, 200, 25);
        panel.add(phoneField);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(40, 135, 350, 25);
        panel.add(messageLabel);

        JButton submitBtn = GUI.createButton("Submit", 160, 170, 100, 30, 15);
        GUI.addHoverButtonEffect(submitBtn, new Color(180, 210, 255));
        panel.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String phone = phoneField.getText().trim();

            boolean isUserDefault = user.equals("Enter username");
            boolean isPhoneDefault = phone.equals("Enter 01x-xxx-xxxx or 01x-xxxx-xxxx");

            if (user.isEmpty() && phone.isEmpty() || isUserDefault && isPhoneDefault) {
                messageLabel.setText("All fields must be filled.");
                return;
            }

            if (!Validation.isValidPhoneNumber(phone)) {
                messageLabel.setText("Invalid phone number format.");
                return;
            }

            if (UserFileManager.isUserExists(user)) {
                if (UserFileManager.checkUserPhoneNumber(user, phone)) {
                    this.username = user;
                    this.verificationCode = generateVerificationCode();
                    codeLabel.setText("Verification Code is: " + verificationCode);
                    cardLayout.show(mainPanel, "VerificationPage");
                } else {
                    messageLabel.setText("Phone number does not match our records.");
                }
            } else {
                messageLabel.setText("Username not found.");
            }
        });

        return panel;
    }

    private JPanel createVerificationPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(220, 240, 255));

        JLabel titleLabel = GUI.createLabel("Verification Code", 140, 20, 200, 25, 18);
        panel.add(titleLabel);

        codeLabel = new JLabel("Verification Code is: ");
        codeLabel.setBounds(50, 60, 300, 25);
        panel.add(codeLabel);

        JLabel inputLabel = new JLabel("Enter Code:");
        inputLabel.setBounds(50, 100, 100, 25);
        panel.add(inputLabel);

        JTextField codeField = GUI.createPlaceholderTextField("Enter verification code", 150, 100, 150, 25);
        panel.add(codeField);

        JLabel messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(150, 130, 300, 25);
        panel.add(messageLabel);

        JButton verifyBtn = GUI.createButton("Verify", 170, 160, 100, 30, 15);
        GUI.addHoverButtonEffect(verifyBtn, new Color(180, 210, 255));
        panel.add(verifyBtn);

        verifyBtn.addActionListener(e -> {
            if (codeField.getText().trim().equals(verificationCode)) {
                cardLayout.show(mainPanel, "NewPasswordPage");
            } else {
                messageLabel.setText("Invalid code. Try again.");
                verificationCode = generateVerificationCode(); // regenerate
                codeLabel.setText("Verification Code is: " + verificationCode); // update label
            }
        });
        return panel;
    }

    private JPanel createNewPasswordPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(220, 240, 255));

        JLabel titleLabel = GUI.createLabel("Set New Password", 130, 20, 250, 25, 18);
        panel.add(titleLabel);

        JLabel passLabel = new JLabel("New Password:");
        passLabel.setBounds(50, 70, 120, 25);
        panel.add(passLabel);

        JPasswordField passField = GUI.createPlaceholderPasswordField("Enter new password", 180, 70, 180, 25);
        panel.add(passField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(50, 110, 120, 25);
        panel.add(confirmLabel);

        JPasswordField confirmField = GUI.createPlaceholderPasswordField("Confirm password again", 180, 110, 180, 25);
        panel.add(confirmField);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(50, 140, 330, 25);
        panel.add(messageLabel);

        JButton updateBtn = GUI.createButton("Update", 150, 180, 100, 30, 15);
        GUI.addHoverButtonEffect(updateBtn, new Color(180, 210, 255));
        panel.add(updateBtn);

        updateBtn.addActionListener(e -> {
            String pass = new String(passField.getPassword()).trim();
            String confirm = new String(confirmField.getPassword()).trim();

            if (pass.isEmpty() || confirm.isEmpty()) {
                messageLabel.setText("Fields cannot be empty.");
                return;
            }

            if (!Validation.isValidPassword(pass)) {
                messageLabel.setText("Password must have 8+ chars, symbol, number.");
                return;
            }

            if (!pass.equals(confirm)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            if (UserFileManager.updateUserPassword(username, pass)) {
                JOptionPane.showMessageDialog(frame, "Password successfully updated!");
                frame.dispose();
                new User_LoginPage();
            } else {
                messageLabel.setText("Failed to update. Try again.");
            }
        });

        return panel;
    }

    private String generateVerificationCode() {
        Random rand = new Random();
        return String.format("%06d", rand.nextInt(1000000));
    }
}
