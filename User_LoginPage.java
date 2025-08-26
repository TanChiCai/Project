import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class User_LoginPage {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final boolean[] isPasswordVisible = {false}; // for reuse
    private JFrame frame;
    private DesktopPage desktopPage;

    public User_LoginPage() {
        frame = new JFrame("Login Page");
        frame.setSize(560, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Dimension screenSize = new Dimension(560, 350);
        JLabel background = GUI.createBackgroundLabel("Image/loginPageBackground.png", screenSize);
        background.setLayout(null);
        background.setPreferredSize(screenSize);
        frame.setContentPane(background);

        JLabel titleLabel = GUI.createLabel("Welcome to User Login Page!", 70, 30, 500, 40, 30);
        background.add(titleLabel);

        JSeparator titleSeparator = GUI.separator(50, 75, 460, 3);
        background.add(titleSeparator);

        JLabel usernameLabel = GUI.createLabel("Username:", 115, 100, 110, 30, 17);
        background.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(210, 105, 240, 25);
        GUI.addTextPlaceholder(usernameField, "Enter username", isPasswordVisible[0]);
        background.add(usernameField);

        JLabel passwordLabel = GUI.createLabel("Password:", 115, 145, 100, 30, 17);
        background.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(210, 150, 169, 25);
        GUI.addPassPlaceholder(passwordField, "Enter password", isPasswordVisible[0]);
        background.add(passwordField);

        JButton toggleButton = GUI.createTogglePasswordButton(passwordField, "Enter password", isPasswordVisible);
        toggleButton.setBounds(380, 150, 70, 25);
        background.add(toggleButton);

        usernameField.addKeyListener(new KeyAdapter() {
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
                new User_ForgotPasswordPage();
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

        JLabel signUpMessage = GUI.createLabel("Don't have an account? ", 180, 285, 200, 25, 13);
        background.add(signUpMessage);

        JLabel signUpLink = GUI.signUpLink(330, 285, 80, 25, 13);
        background.add(signUpLink);

        signUpLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                performSignup();
            }

            public void mouseEntered(MouseEvent e) {
                signUpLink.setText("<html><u><b>Sign Up</b></u></html>");
            }

            public void mouseExited(MouseEvent e) {
                signUpLink.setText("<html><u>Sign Up</u></html>");
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        boolean isUsernamePlaceholder = username.equals("Enter username");
        boolean isPasswordPlaceholder = password.equals("Enter password");

        if (isUsernamePlaceholder || isPasswordPlaceholder || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both username and password.");
            return;
        }

        UserFileManager userFileManager = new UserFileManager();
        UserData loggedInUser = userFileManager.validateUserLogin(username, password);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(frame, "Login Successful!");
            frame.dispose();
            new ValidCheck();
            new User_HomePage(loggedInUser).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            GUI.addTextPlaceholder(usernameField, "Enter username", isPasswordVisible[0]);
            isPasswordVisible[0] = GUI.addPassPlaceholder(passwordField, "Enter password", isPasswordVisible[0]);
        }
    }

    private void performSignup() {
        frame.dispose();
        new User_SignUpPage();
    }
}
