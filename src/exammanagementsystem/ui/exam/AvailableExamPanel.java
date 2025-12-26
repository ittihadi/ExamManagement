package exammanagementsystem.ui.exam;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;
import java.time.Instant;

import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.QuestionDAO;
import exammanagementsystem.dao.ResultDAO;
import exammanagementsystem.dao.ResultDAO.Result;

import javax.swing.table.DefaultTableModel;

public class AvailableExamPanel extends JPanel {

    private ExamDAO examDAO = new ExamDAO();
    private ResultDAO resultDAO = new ResultDAO();
    private QuestionDAO questionDAO = new QuestionDAO();
    private JTable table;
    private DefaultTableModel model;

    public AvailableExamPanel(String userId) {
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new String[] { "Exam ID", "Title", "Start", "End", "Status", "Score" }, 0);
        table = new JTable(model);

        loadData(userId);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData(String userId) {
        try {
            Instant now = Instant.now();

            for (ExamDAO.Exam e : examDAO.readByParticipant(userId)) {
                String status = null;
                Instant start_inst = e.getStartTime().toInstant();
                Instant end_inst = e.getEndTime().toInstant();

                if (now.isBefore(start_inst)) {
                    status = "Upcoming";
                } else if (now.isAfter(end_inst)) {
                    status = "Passed";
                } else {
                    status = "Ongoing";
                }

                int questionNum = questionDAO.readByExam(e.getId()).size();
                float scoreSum = 0;

                java.util.List<Result> userResults = resultDAO.readByExamAndUser(e.getId(), userId);
                for (Result result : userResults) {
                    scoreSum += result.getScore();
                }

                float scoreAvg = 0;
                if (questionNum > 0) {
                    scoreAvg = scoreSum / questionNum;
                }

                model.addRow(new Object[] {
                        e.getId(),
                        e.getTitle(),
                        e.getStartTime(),
                        e.getEndTime(),
                        status,
                        scoreAvg
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed load exams");
        }
    }
}
