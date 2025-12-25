package exammanagementsystem.ui.result;

/**
 *
 * @author ittihadi
 * @author bakthiananda
 */


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import exammanagementsystem.dao.ResultDAO;
import exammanagementsystem.dao.ResultDAO.Result;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ScoreDistributionPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblAvg;
    private JLabel lblMax;
    private JLabel lblMin;

    private ResultDAO resultDAO;

    public ScoreDistributionPanel() {
        resultDAO = new ResultDAO();
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Score Distribution", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        model = new DefaultTableModel(
            new String[]{"Participant", "Final Score", "Status"}, 0
        );
        table = new JTable(model);

        JPanel summaryPanel = new JPanel(new GridLayout(1,3,10,10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));

        lblAvg = new JLabel("Average: -");
        lblMax = new JLabel("Highest: -");
        lblMin = new JLabel("Lowest: -");

        summaryPanel.add(lblAvg);
        summaryPanel.add(lblMax);
        summaryPanel.add(lblMin);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
    }

    /**
     * Dipanggil ketika supervisor memilih exam
     */
    public void setExamContext(int examId) {
        loadScoreDistribution(examId);
    }

    private void loadScoreDistribution(int examId) {
        model.setRowCount(0);

        try {
            List<Result> results = resultDAO.readByExam(examId);

            // total score per user
            Map<String, Float> totalScore = new HashMap<>();

            for (Result r : results) {
                totalScore.merge(
                    r.getUser_id(),
                    r.getScore(),
                    Float::sum
                );
            }

            if (totalScore.isEmpty()) {
                lblAvg.setText("Average: -");
                lblMax.setText("Highest: -");
                lblMin.setText("Lowest: -");
                return;
            }

            float sum = 0;
            float max = Float.MIN_VALUE;
            float min = Float.MAX_VALUE;

            for (Map.Entry<String, Float> entry : totalScore.entrySet()) {
                float score = entry.getValue();
                sum += score;
                max = Math.max(max, score);
                min = Math.min(min, score);

                model.addRow(new Object[]{
                    entry.getKey(),
                    score,
                    score >= 60 ? "Pass" : "Fail"
                });
            }

            lblAvg.setText("Average: " + String.format("%.2f", sum / totalScore.size()));
            lblMax.setText("Highest: " + max);
            lblMin.setText("Lowest: " + min);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed load score distribution");
        }
    }
}
