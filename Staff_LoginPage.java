import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Staff_LoginPage {

    private final JTextField staffIdField;
    private final JPasswordField passwordField;
    private final boolean[] isPasswordVisible = {false};
    private JFrame frame;
    private DesktopPage desktopPage;

    public Staff_LoginPage() {
        frame = new JFrame("Staff Login Page");
        frame.setSize(560, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Dimension screenSize = new Dimension(560, 350);
        JLabel background = GUI.createBackgroundLabel("Image/loginPageBackground.png", screenSize);
        background.setLayout(null);
        background.setPreferredSize(screenSize);
        frame.setContentPane(background);

        JLabel titleLabel = GUI.createLabel("Welcome to Staff Login Page!", 70, 30, 500, 40, 30);
        background.add(titleLabel);

        JSeparator titleSeparator = GUI.separator(50, 75, 460, 3);
        background.add(titleSeparator);

        JLabel idLabel = GUI.createLabel("Staff ID:", 115, 100, 110, 30, 17);
        background.add(idLabel);

        staffIdField = new JTextField();
        staffIdField.setBounds(210, 105, 240, 25);
        GUI.addTextPlaceholder(staffIdField, "Enter staff ID", isPasswordVisible[0]);
        background.add(staffIdField);

        JLabel passwordLabel = GUI.createLabel("Password:", 115, 145, 100, 30, 17);
        background.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(210, 150, 169, 25);
        GUI.addPassPlaceholder(passwordField, "Enter password", isPasswordVisible[0]);
        background.add(passwordField);

        JButton toggleButton = GUI.createTogglePasswordButton(passwordField, "Enter password", isPasswordVisible);
        toggleButton.setBounds(380, 150, 70, 25);
        background.add(toggleButton);

        staffIdField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        JLabel forgotPasswordLabel = GUI.forgotPasswordLabel(210, 176, 140, 20, 12);
        background.add(forgotPasswordLabel);

        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new Staff_ForgotPasswordPage();
            }

            public void mouseEntered(MouseEvent e) {
                forgotPasswordLabel.setText("<html><u><b>Forgot Password?</b></u></html>");
            }

            public void mouseExited(MouseEvent e) {
                forgotPasswordLabel.setText("<html><u>Forgot Password?</u></html>");
            }
        });

        JButton loginButton = GUI.createButton("LOGIN", 220, 210, 120, 35, 14);
        loginButton.setBackground(new Color(34, 177, 76));
        loginButton.setForeground(Color.WHITE);
        background.add(loginButton);

        loginButton.addActionListener(e -> performLogin());

        JSeparator separator = GUI.separator(80, 275, 400, 1);
        background.add(separator);

        // No sign-up for staff
        JLabel infoLabel = GUI.createLabel("Forgot your account? Contact admin.", 150, 285, 300, 25, 13);
        background.add(infoLabel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void performLogin() {
        String staffId = staffIdField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        boolean isIdPlaceholder = staffId.equals("Enter staff ID");
        boolean isPasswordPlaceholder = password.equals("Enter password");

        if (isIdPlaceholder || isPasswordPlaceholder || staffId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both Staff ID and password.");
            return;
        }

        StaffFileManager staffFileManager = new StaffFileManager();
        StaffData loggedInStaff = staffFileManager.validateStaffLogin(staffId, password);

        if (loggedInStaff != null) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            frame.dispose();
            new Staff_HomePage(loggedInStaff).setVisible(true);
            // desktopPage.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Staff ID or password.");
            GUI.addTextPlaceholder(staffIdField, "Enter staff ID", isPasswordVisible[0]);
            isPasswordVisible[0] = GUI.addPassPlaceholder(passwordField, "Enter password", isPasswordVisible[0]);
        }
    

    }
}
