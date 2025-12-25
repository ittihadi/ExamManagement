package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */

import exammanagementsystem.dao.ResultDAO;
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

    private JTable table;
    private DefaultTableModel model;

    private int examId;

    public ScoringPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Per Participant Scoring", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        model = new DefaultTableModel(
            new String[]{
                "User ID", "Question No", "Answer", "Type", "Score"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Score editable hanya ESSAY
                return col == 4 && "ESSAY".equals(getValueAt(row, 3));
            }
        };

        table = new JTable(model);

        JButton btnSave = new JButton("Save Scores");
        btnSave.addActionListener(e -> saveScores());

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }

    // dipanggil dari ExamSupervisionPanel
    public void setExamContext(int examId) {
        this.examId = examId;
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);

        try {
            List<Result> results = resultDAO.readByExam(examId);
            Map<Integer, QuestionType> questionTypes = new HashMap<>();

            for (Question q : questionDAO.readByExam(examId)) {
                questionTypes.put(q.getNumber(), q.getType());
            }

            for (Result r : results) {
                QuestionType type = questionTypes.get(r.getNumber());

                model.addRow(new Object[]{
                    r.getUser_id(),
                    r.getNumber(),
                    r.getAnswer(),
                    type.toString(),
                    r.getScore()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed load scoring data");
        }
    }

    private void saveScores() {
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                String userId = (String) model.getValueAt(i, 0);
                int number = (int) model.getValueAt(i, 1);
                float score = Float.parseFloat(model.getValueAt(i, 4).toString());

                Result r = new Result(
                    examId,
                    userId,
                    number,
                    null,
                    score
                );

                resultDAO.update(r);
            }

            JOptionPane.showMessageDialog(this, "Scores saved");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed save scores");
        }
    }
}
