import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class GUI {

    public static JPanel createBackgroundPanel(String imagePath, Dimension screenSize) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon(imagePath);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);  // Allow absolute positioning of components
        panel.setPreferredSize(screenSize);  // Ensure the panel is the size of the screen
        return panel;
    }

    public static JLabel createBackgroundLabel(String imagePath, Dimension screenSize) {
        ImageIcon backgroundIcon = new ImageIcon(imagePath);
        Image scaledImage = backgroundIcon.getImage().getScaledInstance(screenSize.width, screenSize.height, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        backgroundLabel.setLayout(null);
        return backgroundLabel;
    }

    public static JPanel createImagePanel(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imageLabel, BorderLayout.CENTER);
        panel.setOpaque(false);
        return panel;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height, int fontSize) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(Color.BLACK);
        return label;
    }

    public static JButton createButton(String text, int x, int y, int width, int height, int fontSize) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setFocusPainted(false);
        return button;
    }

    public static void addHoverButtonEffect(JButton button, Color hoverColor) {
        Color originalColor = button.getBackground();
        button.setContentAreaFilled(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    public static void addLinkedHoverEffect(JComponent comp1, JComponent comp2, Runnable onClick, Border hoverBorder, Color hoverColor) {
        MouseAdapter adapter = new MouseAdapter() {
            Border originalBorder1 = comp1 instanceof JPanel ? comp1.getBorder() : null;
            Color originalColor2 = comp2.getForeground();

            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverBorder != null && comp1 instanceof JPanel) comp1.setBorder(hoverBorder);
                if (hoverColor != null && comp2 instanceof JLabel) comp2.setForeground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (comp1 instanceof JPanel) comp1.setBorder(originalBorder1);
                if (comp2 instanceof JLabel) comp2.setForeground(originalColor2);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        };

        comp1.addMouseListener(adapter);
        comp2.addMouseListener(adapter);
    }

    // --- ✅ New: Placeholder Support ---

    public static void addTextPlaceholder(JTextField field, String placeholder, boolean isPasswordVisible) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    public static JButton createTogglePasswordButton(JPasswordField field, String placeholder, boolean[] isVisibleRef) {
        JButton toggleButton = new JButton("Show");
        toggleButton.setFocusPainted(false);

        toggleButton.addActionListener(e -> {
            String pwd = new String(field.getPassword());
            if (!pwd.equals(placeholder)) {
                if (isVisibleRef[0]) {
                    field.setEchoChar('*');
                    toggleButton.setText("Show");
                } else {
                    field.setEchoChar((char) 0);
                    toggleButton.setText("Hide");
                }
                isVisibleRef[0] = !isVisibleRef[0]; // toggle value
            }
        });

        return toggleButton;
    }

    public static boolean addPassPlaceholder(JPasswordField field, String placeholder, boolean isPasswordVisible) {
        field.setEchoChar((char) 0); // Make text visible for placeholder
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                String pwd = new String(field.getPassword());
                if (pwd.equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    // Only set echo char if user wants password hidden
                    if (!isPasswordVisible) {
                        field.setEchoChar('*');
                    } else {
                        field.setEchoChar((char) 0);
                    }
                }
            }

            public void focusLost(FocusEvent e) {
                String pwd = new String(field.getPassword());
                if (pwd.isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0); // Always visible when placeholder is active
                }
            }
        });
        return isPasswordVisible;
    }

    public static JSeparator separator(int x, int y, int width, int height){
        JSeparator separator = new JSeparator();
        separator.setBounds(x, y, width, height);
        separator.setForeground(Color.BLACK);
        return separator;
    }

    public static JLabel forgotPasswordLabel(int x, int y, int width, int height, int size){
        JLabel forgotPasswordLabel = new JLabel("<html><u>Forgot Password?</u></html>");
        forgotPasswordLabel.setForeground(Color.BLUE);
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, size));
        forgotPasswordLabel.setBounds(x, y, width, height);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return forgotPasswordLabel;
    }

    public static JLabel signUpLink(int x, int y, int width, int height, int size){
        JLabel signUpLink = new JLabel("<html><u>Sign Up</u></html>");
        signUpLink.setForeground(Color.BLUE);
        signUpLink.setFont(new Font("Arial", Font.PLAIN, size));
        signUpLink.setBounds(x, y, width, height);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return signUpLink;
    }

    public static JTextField createPlaceholderTextField(String placeholder, int x, int y, int width, int height) {
        JTextField field = new JTextField(placeholder);
        field.setForeground(Color.GRAY);
        field.setBounds(x, y, width, height);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    public static JPasswordField createPlaceholderPasswordField(String placeholder, int x, int y, int width, int height) {
        JPasswordField field = new JPasswordField();
        field.setBounds(x, y, width, height);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0); // Show text instead of bullets
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('•'); // or (char) 8226 for bullet symbol
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    // Create icon
    public static ImageIcon createScaledIcon(String imagePath, int width, int height) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Create icon with text
    public static JButton createIconTextButton(String text, ImageIcon icon, int x, int y, int width, int height, int fontSize) {
        JButton button = new JButton(text, icon);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        return button;
    }

    // Create profile button
    public static JButton createProfileButton(String name, String imagePath, int x, int y, int width, int height) {
        String userShort = name.length() >= 3 ? name.substring(0, 3).toUpperCase() : name.toUpperCase();
        ImageIcon icon = createScaledIcon(imagePath, 28, 28);
        return createIconTextButton(userShort, icon, x, y, width, height, 12);
    }

    // Create staff Id button
    public static JButton createStaffIdButton(String id, String imagePath, int x, int y, int width, int height) {
        ImageIcon staffbtn = createScaledIcon(imagePath, 28, 28);
        return createIconTextButton(id, staffbtn, x, y, width, height, 12);
    }

    // Create notification button
    public static JButton createNotificationButton(String icon,int x,int y,int width,int height) {
        ImageIcon scaledIcon = createScaledIcon(icon,(width-11),(height-10));
        JButton notificationBtn = new JButton(scaledIcon);
        notificationBtn.setBounds(x,y,width,height);
        notificationBtn.setFocusPainted(false);
        notificationBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        notificationBtn.setVerticalTextPosition(SwingConstants.CENTER);
        return notificationBtn;
    }
}
