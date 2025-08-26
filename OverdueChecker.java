import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.*;
import java.awt.*;

public class OverdueChecker {
    private boolean overdueFrameShown = false; // Flag to track if the overdue dialog is shown
    private boolean finishFine = false; // Flag to track if the overdue dialog is shown

    public boolean checkOverdueAndShowFine(BorrowDetail borrow, String userIdText, String bookIdText) {
        LocalDate today = LocalDate.now();
        LocalDate dueDate = borrow.getDueDate();

        if (!overdueFrameShown && today.isAfter(dueDate)) {
            overdueFrameShown = true;

            ProcessFineDetail fineDetail = new ProcessFineDetail();
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
            int payment = (int) daysOverdue; // RM1 per day

            
            showOverdueDialog(userIdText, bookIdText, payment, fineDetail, borrow);
           
            
            if(finishFine){
                return false;
            }
            
        }
        return true;
    }

    private void showOverdueDialog(String userIdText, String bookIdText, int payment, ProcessFineDetail p, BorrowDetail borrow) {
        JDialog overdueDialog = new JDialog((Frame) null, "Process Fine", true);
        overdueDialog.setSize(1000, 400);
        overdueDialog.setLocationRelativeTo(null);
        overdueDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        overdueDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel warningLabel = new JLabel("⚠️ Late return. Fine amount: RM" + payment, SwingConstants.CENTER);
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        warningLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        contentPanel.add(warningLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel details = new JLabel("Please choose whether to pay the fine now or later.", SwingConstants.CENTER);
        details.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(details);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JButton laterPay = new JButton("Pay Later");
        laterPay.addActionListener(e -> {
            p.setPayStatus(false);
            // Once the fine is recorded, show the return book page
            ProcessFinePage.showFinePage(userIdText, bookIdText, false, payment);
            JOptionPane.showMessageDialog(overdueDialog, "Fine has been recorded.");
            
            finishFine=true;
            overdueDialog.dispose();
        });

        JButton fineButton = new JButton("Pay Now");
        fineButton.addActionListener(e -> {
            p.setPayStatus(true);
            // Once the fine is paid, show the return book page
            ProcessFinePage.showFinePage(userIdText, bookIdText, true, payment);
            JOptionPane.showMessageDialog(null, "✅ Payment completed successfully!", "Payment Status", JOptionPane.INFORMATION_MESSAGE);
            // Proceed to return book dialog after fine
            
            finishFine=true;
            overdueDialog.dispose();
        });

        buttonPanel.add(laterPay);
        buttonPanel.add(fineButton);
        contentPanel.add(buttonPanel);

        overdueDialog.add(contentPanel, BorderLayout.CENTER);
        overdueDialog.setVisible(true); // Show fine dialog
    }  
}
