package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class ResultPerParticipantPanel extends JPanel {

    public ResultPerParticipantPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("My Exam Results", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JTable resultTable = new JTable(
            new Object[][]{
                {"Mid Exam", "1", "A", "Correct", "10"},
                {"Mid Exam", "2", "Essay Answer", "Pending", "-"}
            },
            new String[]{
                "Exam Title", "Question No",
                "Answer", "Status", "Score"
            }
        );

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
    }
}