package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class ScoringPanel extends JPanel {

    public ScoringPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Per Participant Scoring", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JTable scoringTable = new JTable(
            new Object[][]{
                {"E001", "U001", "Alice", "1", "A", "Correct", "10"},
                {"E001", "U002", "Bob", "2", "Essay Answer", "Manual", ""}
            },
            new String[]{
                "Exam ID", "User ID", "Participant",
                "Question No", "Answer", "Status", "Score"
            }
        );

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JButton("Save Scores"));

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(scoringTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setExamContext(String examId) {
    // nanti: filter scoring berdasarkan examId
    }
}