import java.awt.*;
import javax.swing.*;

public class User_SignUpPage extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    private final JLabel background;

    private final JTextField phoneField = new JTextField();
    private final JTextField usernameField = new JTextField();

    private final JPasswordField passwordField = new JPasswordField();
    private final JPasswordField confirmField = new JPasswordField();
    private final boolean[] isPasswordVisible = {false};
    private final boolean[] isConfirmVisible = {false};

    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JLabel progressLabel = new JLabel("");

    private final String passPlaceholder = "Enter Password";
    private final String confirmPlaceholder = "Confirm Password";

    public User_SignUpPage() {
        setTitle("User Sign Up");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Set up background with layout null
        Dimension screenSize = new Dimension(600, 500);
        background = GUI.createBackgroundLabel("Image/signUpBackground.png", screenSize);
        background.setLayout(null);
        background.setPreferredSize(screenSize);
        setContentPane(background);

        progressBar.setBounds(40, 20, 500, 20);
        progressBar.setValue(33);
        progressLabel.setBounds(240, 20, 300, 20);
        progressLabel.setText("You have done 33%");
        background.add(progressLabel);
        background.add(progressBar);

        // 3. Add card panel on top of background
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(0, 0, 0, 0));
        cardPanel.setOpaque(false);
        cardPanel.setBounds(0, 50, 600, 400);
        background.add(cardPanel);

        cardPanel.add(step1Panel(), "Step1");
        cardPanel.add(step2Panel(), "Step2");
        cardPanel.add(step3Panel(), "Step3");

        setVisible(true);
    }

    private JPanel step1Panel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0, 0, 0, 0));
        cardPanel.setOpaque(false);

        JLabel step1Label = GUI.createLabel("Step 1 : Fill in Phone Number and Username", 45, 0, 500, 80, 23);
        panel.add(step1Label); 

        JLabel phoneLabel = GUI.createLabel("Phone Number : ", 70, 80, 120, 30, 15);
        panel.add(phoneLabel); 

        phoneField.setBounds(190, 80, 300, 30);
        GUI.addTextPlaceholder(phoneField, "Enter Phone Number", false);
        panel.add(phoneField);

        JLabel usernameLabel = GUI.createLabel("Username : ", 70, 140, 120, 30, 15);
        panel.add(usernameLabel); 

        usernameField.setBounds(190, 140, 300, 30);
        GUI.addTextPlaceholder(usernameField, "Enter Username", false);
        panel.add(usernameField);

        // Reminder
        JLabel reminder = GUI.createLabel("Reminder:",45,190,100,30,16);
        JLabel rmdphoneformat = GUI.createLabel("1. Phone Number: 01x-xxx-xxxx or 01x-xxxx-xxxx",45,215,490,30,15);
        JLabel rmdusername = GUI.createLabel("2. Username: 4-35 characters (letters, numbers, or _)",45,240,510,30,15);
        panel.add(reminder);
        panel.add(rmdphoneformat);
        panel.add(rmdusername);

        JButton nextBtn = GUI.createButton("Next", 400, 330, 150, 50, 31);
        nextBtn.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();

            boolean isPhoneDefault = phone.equals("Enter Phone Number");
            boolean isUsernameDefault = username.equals("Enter Username");

            if (phone.isEmpty() && username.isEmpty() || isPhoneDefault && isUsernameDefault){
                JOptionPane.showMessageDialog(this, "All field must be fill!");
                return;
            }

            if (!Validation.isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(this, "Invalid phone number!");
                return;
            }

            if (UserFileManager.isPhoneNumberExist(phone)) {
                JOptionPane.showMessageDialog(this, "Phone number already registered.");
                return;
            }

            if (username.equals("") || isUsernameDefault || username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
                return;
            }

            progressLabel.setText("You have done 66%");
            progressBar.setValue(66);
            background.revalidate();
            background.repaint();
            cardLayout.show(cardPanel, "Step2");
        });
        panel.add(nextBtn);

        JButton backBtn = GUI.createButton("Back to Login Page", 20, 330, 300, 50, 27);
        backBtn.addActionListener(e -> {
            dispose();
            new User_LoginPage();
        });
        panel.add(backBtn);

        return panel;
    }

    private JPanel step2Panel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0, 0, 0, 0));
        cardPanel.setOpaque(false);

        JLabel step2Label = GUI.createLabel("Step 2 : Create Strong Password", 100, 0, 500, 80, 23);
        panel.add(step2Label); 

        JLabel pswLabel = GUI.createLabel("Enter Password : ", 40, 80, 150, 30, 15);
        panel.add(pswLabel); 

        passwordField.setBounds(180, 80, 300, 30);
        GUI.addPassPlaceholder(passwordField, passPlaceholder, isPasswordVisible[0]);
        panel.add(passwordField);

        JButton togglePass = GUI.createTogglePasswordButton(passwordField, passPlaceholder, isPasswordVisible);
        togglePass.setBounds(480, 80, 80, 30);
        panel.add(togglePass);

        JLabel confirmPswLabel = GUI.createLabel("Confirm Password : ", 40, 130, 150, 30, 15);
        panel.add(confirmPswLabel); 

        confirmField.setBounds(180, 130, 300, 30);
        GUI.addPassPlaceholder(confirmField, confirmPlaceholder, isConfirmVisible[0]);
        panel.add(confirmField);

        JButton toggleConfirm = GUI.createTogglePasswordButton(confirmField, confirmPlaceholder, isConfirmVisible);
        toggleConfirm.setBounds(480, 130, 80, 30);
        panel.add(toggleConfirm);

        // Reminder
        JLabel reminder2 = GUI.createLabel("Reminder:",45,190,100,30,16);
        JLabel passwordformat = GUI.createLabel("Password: 8+ chars with letters, numbers, symbols",45,215,490,30,15);
        panel.add(reminder2);
        panel.add(passwordformat);

        JButton nextBtn = GUI.createButton("Sign Up", 400, 330, 150, 50, 28);
        nextBtn.addActionListener(e -> {
            String pwd = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (!Validation.isValidPassword(pwd)) {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters and include letters, numbers, and symbols.");
                return;
            }

            if (!Validation.doPasswordsMatch(pwd, confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                return;
            }

            String userid=UserData.generateUserid();
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();
            UserData user = new UserData(userid,username, pwd, phone);
            UserFileManager.saveUser(user);

            progressLabel.setBounds(200, 20, 300, 20);
            progressLabel.setText("Congrats, you have finish Sign Up!");
            progressBar.setValue(100);
            background.revalidate();
            background.repaint();
            cardLayout.show(cardPanel, "Step3");
        });
        panel.add(nextBtn);

        JButton backBtn = GUI.createButton("Back to Login Page", 20, 330, 300, 50, 27);
        backBtn.addActionListener(e -> {
            dispose();
            new User_LoginPage();
        });
        panel.add(backBtn);

        return panel;
    }

    private JPanel step3Panel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0, 0, 0, 0)); // Transparent panel
        cardPanel.setOpaque(false);

        JPanel imagePanel = GUI.createImagePanel("Image/signUpIcon.png", 70, 70);
        imagePanel.setBounds(260, 70, 70, 70);
        panel.add(imagePanel);

        JLabel successLabel = GUI.createLabel("Sign Up Complete!", 120, 140, 400, 50, 40);

        panel.add(successLabel);

        JButton loginBtn = GUI.createButton("Back to Login Page", 150, 330, 300, 50, 27);
        loginBtn.addActionListener(e -> {
            dispose();
            new User_LoginPage();
        });
        panel.add(loginBtn);

        return panel;
    }
}