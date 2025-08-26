import java.awt.*;
import javax.swing.*;

public class MainReserveRoomGUI {
    public static void reserveRoom(UserData loggedInUser) {
        JFrame frame = new JFrame("Library System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(1430, 700);
        frame.setLayout(new BorderLayout());
        
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        HomeRoomPageGUI homePage = new HomeRoomPageGUI(cardLayout, cardPanel, loggedInUser);

        // Adding different pages
        cardPanel.add(homePage, "Home");
        cardPanel.add(new DiscussionRoomPageGUI(cardLayout, cardPanel, loggedInUser), "DiscussionRoom");
        cardPanel.add(new ConferenceRoomPageGUI(cardLayout, cardPanel, loggedInUser), "ConferenceRoom");
        cardPanel.add(new ReserveDiscussionRoomGUI(cardLayout, cardPanel, loggedInUser), "ReserveDiscussionRoom");
        cardPanel.add(new ReserveConferenceRoomGUI(cardLayout, cardPanel, loggedInUser), "ReserveConferenceRoom");
        cardPanel.add(new ReserveGuidelinePageGUI(cardLayout, cardPanel), "ReserveGuideline");

        frame.add(cardPanel);
        frame.setVisible(true);

        // Listen for the window closing
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Open the library homepage again when reservation frame is closed
                new User_HomePage(loggedInUser);
            }
        });
        
    }
}
