package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import javax.swing.*;
import java.awt.*;
import exammanagementsystem.dao.ExamDAO;
import exammanagementsystem.dao.ResultDAO;
import javax.swing.table.DefaultTableModel;

public class ResultPerParticipantPanel extends JPanel {

    private JComboBox<ExamDAO.Exam> cbExam;
    private JTable table;
    private DefaultTableModel model;

    private ExamDAO examDAO = new ExamDAO();
    private ResultDAO resultDAO = new ResultDAO();

    public ResultPerParticipantPanel(String userId) {
        setLayout(new BorderLayout(10,10));

        cbExam = new JComboBox<>();
        loadExamList(userId);

        cbExam.addActionListener(e -> loadResult(userId));

        model = new DefaultTableModel(
            new String[]{"Question", "Answer", "Score"}, 0
        );
        table = new JTable(model);

        add(cbExam, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadExamList(String userId) {
        try {
            for (ExamDAO.Exam e : examDAO.readByParticipant(userId)) {
                cbExam.addItem(e);
            }
        } catch (Exception ignored) {}
    }

    private void loadResult(String userId) {
        model.setRowCount(0);
        ExamDAO.Exam exam = (ExamDAO.Exam) cbExam.getSelectedItem();
        if (exam == null) return;

        try {
            for (ResultDAO.Result r : resultDAO.readByExamAndUser(exam.getId(), userId)) {
                model.addRow(new Object[]{
                    r.getNumber(),
                    r.getAnswer(),
                    r.getScore()
                });
            }
        } catch (Exception ignored) {}
    }
}
