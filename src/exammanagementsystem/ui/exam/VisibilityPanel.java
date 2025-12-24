package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class VisibilityPanel extends JPanel {

    private JLabel contextLabel;

    public VisibilityPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        contextLabel = new JLabel("Visibility Settings (All Exams)");
        contextLabel.setFont(contextLabel.getFont().deriveFont(Font.BOLD));

        JCheckBox showCorrect = new JCheckBox("Show Correct / Incorrect");
        JCheckBox showAnswer = new JCheckBox("Show Real Answer");
        JCheckBox showScore = new JCheckBox("Show Score");

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
        options.add(showCorrect);
        options.add(showAnswer);
        options.add(showScore);

        add(contextLabel, BorderLayout.NORTH);
        add(options, BorderLayout.CENTER);
    }

    public void setExamContext(String examId) {
        if (examId == null) {
            contextLabel.setText("Visibility Settings (All Exams)");
        } else {
            contextLabel.setText("Visibility Settings for Exam: " + examId);
        }
    }
}