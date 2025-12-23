package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class SupervisorListPanel extends JPanel {

    public SupervisorListPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Supervisors for Selected Exam", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JTable table = new JTable(
            new Object[][]{
                {"S001", "Dr. John"},
                {"S002", "Ms. Sarah"}
            },
            new String[]{
                "User ID", "Supervisor Name"
            }
        );

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Add Supervisor"));
        buttons.add(new JButton("Remove Supervisor"));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}