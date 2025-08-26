import java.awt.*;
import javax.swing.*;

public class DesktopPage {
    JFrame frame;
    private static LibraryPage activeLibraryPage;

    public DesktopPage() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("Desktop");
        frame.setSize(screenSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JLabel backgroundPanel = GUI.createBackgroundLabel("Image/desktopBackground.png", screenSize);
        backgroundPanel.setLayout(null);
        backgroundPanel.setPreferredSize(screenSize);

        JPanel imagePanel = GUI.createImagePanel("Image/libraryImage.png", 70, 70);
        imagePanel.setBounds(20, 20, 70, 70);

        JLabel libraryLabel = GUI.createLabel("Library", 29, 90, 70, 30, 14);

        // Hover effect applied to both together
        GUI.addLinkedHoverEffect(imagePanel, libraryLabel,
                () -> {
                    if (activeLibraryPage != null) {
                        activeLibraryPage.dispose();
                    }
                    activeLibraryPage = new LibraryPage();
                },
                BorderFactory.createLineBorder(Color.BLUE, 3),
                Color.BLUE
        );

        backgroundPanel.add(imagePanel);
        backgroundPanel.add(libraryLabel);

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    public void dispose(){
        frame.dispose();
    }
}

