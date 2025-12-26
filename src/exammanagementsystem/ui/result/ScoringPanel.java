package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import exammanagementsystem.dao.ResultDAO;
import exammanagementsystem.dao.UserDAO;
import exammanagementsystem.dao.ResultDAO.Result;
import exammanagementsystem.dao.QuestionDAO;
import exammanagementsystem.dao.QuestionDAO.Question;
import exammanagementsystem.dao.QuestionDAO.QuestionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Font;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ScoringPanel extends JPanel {

    private final ResultDAO resultDAO = new ResultDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final UserDAO userDAO = new UserDAO();

    private JTable table;
    private DefaultTableModel model;
    private JLabel txtStatus;

    private int examId;

    public ScoringPanel(int examId) {
        this.examId = examId;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Scoring Overview", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        txtStatus = new JLabel();
        txtStatus.setAlignmentX(0);

        model = new DefaultTableModel(
                new String[] {
                        "Question No", "Average Score", "Highest Score", "Lowest Score"
                }, 0) {
        };

        table = new JTable(model);
        loadData();

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(txtStatus, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            List<Question> questions = questionDAO.readByExam(examId);
            int participantCount = userDAO.readParticipantsByExamId(examId).size();

            txtStatus.setText(String.format(
                    "Score overview for %d questions, with %d participants",
                    questions.size(),
                    participantCount));

            for (Question question : questions) {

                List<Result> qsResults = resultDAO.readByExam(examId)
                        .stream()
                        .filter(r -> r.getNumber() == question.getNumber())
                        .toList();

                float maxScore = 0.0f;
                float minScore = Float.MAX_VALUE;
                float sumScore = 0.0f;

                for (Result res : qsResults) {
                    float score = res.getScore();
                    if (maxScore < score) {
                        maxScore = score;
                    }
                    if (minScore > score) {
                        minScore = score;
                    }
                    sumScore += score;
                }

                float scoreAvg = sumScore / participantCount;
                System.out.println(scoreAvg);

                model.addRow(new Object[] {
                        question.getNumber(),
                        scoreAvg,
                        maxScore,
                        minScore
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed load scoring data");
        }
    }
}
