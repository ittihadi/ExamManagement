package exammanagementsystem.ui.question;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;

public class QuestionSetPanel extends JPanel {

    private String examId;

    private JLabel contextLabel;
    private JTable questionTable;

    public QuestionSetPanel() {
        setLayout(new BorderLayout(10, 10));
        initUI();
    }

    private void initUI() {
        contextLabel = new JLabel("All Questions");
        contextLabel.setFont(contextLabel.getFont().deriveFont(Font.BOLD));

        questionTable = new JTable(
            new Object[][]{
                {"Q001", "What is Java?", "Objective"},
                {"Q002", "Explain OOP", "Essay"}
            },
            new String[]{"ID", "Question", "Type"}
        );

        add(contextLabel, BorderLayout.NORTH);
        add(new JScrollPane(questionTable), BorderLayout.CENTER);
    }

    /* ========= DIPANGGIL DARI ExamManagementPanel ========= */


    public void setExamContext(String examId) {
        this.examId = examId;
        updateContextLabel();
    }

    private void updateContextLabel() {
        if (examId == null) {
            contextLabel.setText("All Questions");
        } else {
            contextLabel.setText("Questions for Exam: " + examId);
        }
    }
}