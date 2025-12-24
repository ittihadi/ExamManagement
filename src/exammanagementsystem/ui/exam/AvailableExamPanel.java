package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class AvailableExamPanel extends JPanel {

    public AvailableExamPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Available Exams", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16));

        JTable examTable = new JTable(
            new Object[][]{
                {"Mid Exam", "2025-06-01 09:00", "2025-06-01 11:00", "Finished"},
                {"Final Exam", "2025-07-01 13:00", "2025-07-01 15:00", "Upcoming"}
            },
            new String[]{
                "Exam Title", "Start Time", "End Time", "Status"
            }
        );

        JButton viewResultBtn = new JButton("View Result");

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(viewResultBtn);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(examTable), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}