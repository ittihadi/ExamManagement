package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class ParticipantListPanel extends JPanel {

    public ParticipantListPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Participants in Selected Exam", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JTable table = new JTable(
            new Object[][]{
                {"U001", "Alice"},
                {"U002", "Bob"},
                {"U003", "Charlie"}
            },
            new String[]{
                "User ID", "Participant Name"
            }
        );

        JPanel buttons = new JPanel();
        buttons.add(new JButton("Add Participant"));
        buttons.add(new JButton("Remove Participant"));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}