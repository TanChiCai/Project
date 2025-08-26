import java.awt.*;
import javax.swing.*;

public class LibraryPage {
    JFrame frame;

    public LibraryPage() {
        frame = new JFrame("Library");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Dimension screenSize = new Dimension(400, 300); // Match your frame size
        JLabel background = GUI.createBackgroundLabel("Image/libraryBackground.png", screenSize); // Replace with your actual image path
        background.setLayout(null);
        background.setPreferredSize(screenSize);

        JLabel titleLabel = GUI.createLabel("Login with?", 110, 10, 230, 60, 30);
        background.add(titleLabel);

        JButton userBtn = GUI.createButton("User", 90, 90, 200, 50, 27);
        JButton staffBtn = GUI.createButton("Staff", 90, 160, 200, 50, 27);

        background.add(userBtn);
        background.add(staffBtn);

        Color hoverColor = new Color(173, 216, 230); // Light Blue
        GUI.addHoverButtonEffect(userBtn, hoverColor);
        GUI.addHoverButtonEffect(staffBtn, hoverColor);

        userBtn.addActionListener(e -> {
            frame.dispose();
            new User_LoginPage();
        });

        staffBtn.addActionListener(e -> {
            frame.dispose();
            new Staff_LoginPage();
        });

        frame.setContentPane(background);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void dispose(){
        if (frame != null) {
            frame.dispose();
        }
    }
}
